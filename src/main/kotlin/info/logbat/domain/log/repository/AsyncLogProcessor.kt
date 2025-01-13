package info.logbat.domain.log.repository

import info.logbat.common.event.EventConsumer
import info.logbat.domain.log.domain.Log
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.util.function.Consumer
import kotlin.coroutines.coroutineContext

class AsyncLogProcessor (
    private val eventConsumer : EventConsumer<Log>
){
    private val log = LoggerFactory.getLogger(AsyncLogProcessor::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)

    fun init(saveFunction: Consumer<List<Log>>){
        scope.launch {
            leaderTask(saveFunction)
        }
    }

    private suspend fun leaderTask(saveFunction: Consumer<List<Log>>){
        while (coroutineContext.isActive) {
            val element = withContext(Dispatchers.IO) {
                eventConsumer.consume()
            }
            withContext(Dispatchers.IO) {
                saveFunction.accept(element)
            }
        }
    }
}