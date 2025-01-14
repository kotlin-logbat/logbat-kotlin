package info.logbat.domain.log.application

import info.logbat.common.event.EventProducer
import info.logbat.domain.log.domain.Log

//@Service
class LogService2 (
    private val producer : EventProducer<Log>
){

}