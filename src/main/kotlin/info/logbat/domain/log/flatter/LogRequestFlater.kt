package info.logbat.domain.log.flatter

import info.logbat.common.event.EventProducer
import info.logbat.domain.log.domain.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class LogRequestFlater (
    private val eventProducer: EventProducer<Log>
){
    private val scope = CoroutineScope(Dispatchers.Default)
    fun flatten(logs: List<Log>){
        scope.launch {
            eventProducer.produce(logs)
        }
    }
}