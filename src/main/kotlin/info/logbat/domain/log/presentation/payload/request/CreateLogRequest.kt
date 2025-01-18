package info.logbat.domain.log.presentation.payload.request

import info.logbat.domain.log.domain.Log
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateLogRequest(
    val level: String,
    val data: String,
    val timestamp: LocalDateTime
) {
    init {
        require(level.isNotBlank()) { "로그 레벨이 비어있습니다." }
        require(data.isNotBlank()) { "로그 데이터가 비어있습니다." }
        requireNotNull(timestamp) { "타임스탬프가 비어있습니다." }
    }
    fun toEntity(appId: Long): Log {
        return Log.of(appId, level, data, timestamp)
    }
}
