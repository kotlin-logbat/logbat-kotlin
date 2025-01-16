package info.logbat.domain.log.application.payload.request

import info.logbat.domain.log.presentation.payload.request.CreateLogRequest
import java.time.LocalDateTime

data class CreateLogServiceRequest(
    val appKey: String,
    val level: String,
    val data: String,
    val timestamp: LocalDateTime
){
    companion object {
        fun of (appKey: String, request: CreateLogRequest): CreateLogServiceRequest{
            return CreateLogServiceRequest(
                appKey, request.level, request.data, request.timestamp
            )
        }
    }

}
