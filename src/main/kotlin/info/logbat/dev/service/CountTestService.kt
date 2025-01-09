package info.logbat.dev.service

interface CountTestService {

    fun increaseSuccessCount()
    fun increaseErrorCount()
    fun getSuccessCount(): Long
    fun getErrorCount(): Long
    fun reset()
}