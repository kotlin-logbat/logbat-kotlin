package info.logbat.domain.log.queue

import info.logbat.common.event.EventConsumer
import info.logbat.common.event.EventProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.LinkedList
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Scope("prototype")
@Component
class ReentrantLogQueue<T>(
    @Value("\${jdbc.async.timeout}") private val timeout: Long,
    @Value("\${jdbc.async.bulk-size}") private val bulkSize: Int
) : EventProducer<T>, EventConsumer<T> {

    private val queue = LinkedList<T>()
    private val bulkLock = ReentrantLock()
    private val bulkCondition: Condition = bulkLock.newCondition()
    /**
     * Consumer should be one thread
     */
    override fun consume(): List<T> {
        val result = mutableListOf<T>()
        bulkLock.withLock {
            try {
                // Case 1: Full Flush
                if (queue.size >= bulkSize) {
                    repeat(bulkSize) {
                        result.add(queue.poll())
                    }
                    return result
                }
                // Case 2: Blocking
                do {
                    bulkCondition.awaitNanos(TimeUnit.MILLISECONDS.toNanos(timeout))
                } while (queue.isEmpty())

                // Retrieve up to `bulkSize` elements
                repeat(bulkSize) {
                    result.add(queue.poll() ?: return@repeat)
                }
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        return result
    }
    /**
     * Producer should be one thread
     */
    override fun produce(data: List<T>) {
        bulkLock.withLock {
            queue.addAll(data)
            if (queue.size >= bulkSize) {
                bulkCondition.signal()
            }
        }
    }
}
