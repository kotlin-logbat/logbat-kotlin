package info.logbat.domain.log.queue

import info.logbat.common.event.EventConsumer
import info.logbat.common.event.EventProducer
import kotlinx.coroutines.sync.Mutex
import org.springframework.beans.factory.annotation.Value
import java.util.concurrent.locks.LockSupport

class LogQueue<T>(
    @Value("\${jdbc.async.timeout}") timeoutMillis: Long,
    @Value("\${jdbc.async.bulk-size}") private val bulkSize: Int
) : EventProducer<T>, EventConsumer<T> {

    private val queue: MutableList<T> = mutableListOf()
    private val timeoutNanos = timeoutMillis * 1_000_000 // Convert to nanoseconds
    @Volatile
    private var consumerThread: Thread? = null
    private val mutex = Mutex() // For thread safety

    /**
     * 큐에서 요소를 일괄적으로 꺼내 반환합니다.
     * 큐에 충분한 요소가 없을 경우 대기하며, 요소가 추가되면 처리합니다.
     */
    override fun consume(): List<T> {
        val result = mutableListOf<T>()
        // Fast path: enough elements are available
        if (queue.size >= bulkSize) {
            repeat(bulkSize) {
                result.add(queue.removeAt(0))
            }
            return result
        }
        consumerThread = Thread.currentThread()

        do {
            LockSupport.parkNanos(timeoutNanos)
        } while (queue.isEmpty())

        repeat(bulkSize) {
            if (queue.isNotEmpty()) {
                result.add(queue.removeAt(0))
            }
        }

        consumerThread = null
        return result
    }

    /**
     * 큐에 요소를 추가하고, 소비자 스레드를 깨웁니다.
     */
    override fun produce(data: List<T>) {
        synchronized(queue) {
            queue.addAll(data)
            if (consumerThread != null && queue.size >= bulkSize) {
                LockSupport.unpark(consumerThread)
            }
        }
    }
}