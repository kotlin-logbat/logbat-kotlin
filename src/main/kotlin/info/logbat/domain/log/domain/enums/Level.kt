package info.logbat.domain.log.domain.enums

enum class Level {
    TRACE, // 0
    DEBUG, // 1
    INFO, // 2
    WARN, // 3
    ERROR; // 4

    companion object {
        fun from(level: String?): Level {
            require(!level.isNullOrBlank()) { "level은 null이거나 빈 문자열일 수 없습니다." }
            val upperCaseLevel = level.trim().uppercase()
            return entries.find { it.name == upperCaseLevel }
                ?: throw IllegalArgumentException("level이 올바르지 않습니다.")
        }
        fun from(level: Int?): Level {
            val levelInt = checkNotNull(level) { "level은 null이 될 수 없습니다." }
            return entries.find { it.ordinal == levelInt }
                ?: throw IllegalArgumentException("level이 올바르지 않습니다.")
        }
    }

}