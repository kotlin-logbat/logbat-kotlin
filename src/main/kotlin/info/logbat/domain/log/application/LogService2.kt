package info.logbat.domain.log.application

import info.logbat.common.event.EventProducer
import info.logbat.domain.log.domain.Log
import info.logbat.domain.log.presentation.payload.request.CreateLogRequest
import info.logbat.domain.project.application.AppService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LogService (
    private val producer : EventProducer<Log>,
    private val appService: AppService
){
    private val log = LoggerFactory.getLogger(LogService::class.java)

    fun saveLogs(appKey:String, requests:List<CreateLogRequest>){
        val appId = appService.getAppIdByToken(appKey)
        val logs = requests.mapNotNull{ request ->
            runCatching { request.toEntity(appId) }
                .onFailure { e -> log.error("Failed to convert request to entity: {}", request, e) }
                .getOrNull()
        }
        if (logs.isNotEmpty()) {
            producer.produce(logs)
        } else {
            log.warn("No valid logs to produce. All conversions failed.")
        }
    }
}