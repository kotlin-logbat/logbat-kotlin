package info.logbat.domain.log.application

import info.logbat.common.event.EventProducer
import info.logbat.domain.log.domain.Log
import org.springframework.stereotype.Service

@Service
class LogService (
    private val producer : EventProducer<Log>
){

}