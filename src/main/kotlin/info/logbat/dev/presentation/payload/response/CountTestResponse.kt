package info.logbat.dev.presentation.payload.response

data class CountTestResponse(
    val successCount: Long,
    val failedCount: Long,
){
    companion object {
        @JvmStatic
        fun of(successCount:Long, failedCount:Long) = CountTestResponse(successCount, failedCount)
    }
}
