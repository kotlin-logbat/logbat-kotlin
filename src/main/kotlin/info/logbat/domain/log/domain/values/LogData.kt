package info.logbat.domain.log.domain.values

data class LogData private constructor(val value: String) {

    companion object {
        @JvmStatic
        fun from(data: String): LogData {
            return LogData(data)
        }
    }

    init {
        validateData(value)
    }

    private fun validateData(data: String) {
        require(data.isNotEmpty()) { "log data는 null일 수 없고 빈 문자열일 수 없습니다." }
    }
}
