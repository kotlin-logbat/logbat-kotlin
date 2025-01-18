package info.logbat.domain.log.repository

import com.zaxxer.hikari.HikariDataSource
import info.logbat.common.event.EventProducer
import info.logbat.domain.log.domain.Log
import info.logbat.domain.log.queue.ReentrantLogQueue
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.context.annotation.Lazy
import java.util.concurrent.ThreadLocalRandom
import kotlin.coroutines.coroutineContext

@Primary
@Component
class AsyncMultiProcessor<E>(
    @Value("\${queue.count:3}") private val queueCount: Int,
    @Value("\${jdbc.async.timeout:5000}") private val timeout: Long,
    @Value("\${jdbc.async.bulk-size:3000}") private val bulkSize: Int,
    jdbcTemplate: JdbcTemplate,
    private val objectProvider: ObjectProvider<ReentrantLogQueue<E>>
) : EventProducer<E> {

    private val queues: MutableList<ReentrantLogQueue<E>> = mutableListOf()
    private val saveFunctionMutex = Mutex()
    private lateinit var saveFunction: suspend (List<E>) -> Unit
    private val scope = CoroutineScope(Dispatchers.Default)
    private val log = LoggerFactory.getLogger(AsyncMultiProcessor::class.java)

    init {
        val poolSize = getPoolSize(jdbcTemplate)
        setup(queueCount, timeout, bulkSize, poolSize)
    }

    @PostConstruct
    fun initSaveFunction() {
        saveFunction = { throw IllegalStateException("saveFunction not initialized") }
    }

    fun init(saveFunction: suspend (List<E>) -> Unit) {
        this.saveFunction = saveFunction
    }

    override fun produce(data: List<E>) {
        if (data.isEmpty()) return
        val selectedQueue = ThreadLocalRandom.current().nextInt(queueCount)
        scope.launch {
            queues[selectedQueue].produce(data)
        }
    }

    private fun setup(queueCount: Int, timeout: Long, bulkSize: Int, poolSize: Int) {
        repeat(queueCount) {
            val queue = objectProvider.getObject(timeout, bulkSize)
            queues.add(queue)
            scope.launch {
                leaderTask(queue)
            }
        }
    }

    private suspend fun leaderTask(queue: ReentrantLogQueue<E>) {
        while (coroutineContext.isActive) {
            val element = withContext(Dispatchers.IO) {
                queue.consume()
            }
            withContext(Dispatchers.IO) {
                saveFunctionMutex.withLock {
                    saveFunction(element)
                }
            }
        }
    }

    private fun getPoolSize(jdbcTemplate: JdbcTemplate): Int {
        val dataSource = jdbcTemplate.dataSource
            ?: throw IllegalArgumentException("DataSource is null")
        if (dataSource !is HikariDataSource) {
            throw IllegalArgumentException("Expected HikariDataSource but got ${dataSource::class.simpleName}")
        }
        val poolSize = dataSource.maximumPoolSize
        log.debug("Creating AsyncMultiProcessor with pool size: {}", poolSize)
        return (poolSize * 5) / 10
    }

    fun submitLog(expectedLog: Log) {

    }
}