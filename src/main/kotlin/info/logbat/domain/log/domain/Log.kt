package info.logbat.domain.log.domain

import info.logbat.domain.log.domain.enums.Level
import info.logbat.domain.log.domain.values.LogData
import java.time.LocalDateTime

data class Log(
    val id: Long? = null,
    val appId: Long,
    val level: Level,
    val data: LogData,
    val timestamp: LocalDateTime
) {
    init {
        validateAppId(appId)
        validateTimestamp(timestamp)
    }

    companion object {
        fun of(appId: Long, level: String, logData: String, timestamp: LocalDateTime): Log {
            return Log(appId = appId, level = Level.from(level), data = LogData.from(logData), timestamp = timestamp)
        }
    }

    constructor(appId: Long, level: String, data: String, timestamp: LocalDateTime) : this(
        id = null,
        appId = appId,
        level = Level.from(level),
        data = LogData.from(data),
        timestamp = timestamp
    )

    private fun validateAppId(appId: Long) {
        require(appId > 0) { "appId는 null일 수 없고 0보다 커야 합니다." }
    }

    private fun validateTimestamp(timestamp: LocalDateTime) {
        requireNotNull(timestamp) { "timestamp는 null이 될 수 없습니다." }
    }
}
