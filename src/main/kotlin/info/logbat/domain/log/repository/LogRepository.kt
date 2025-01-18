package info.logbat.domain.log.repository

import info.logbat.domain.log.domain.Log


interface LogRepository {
    fun save(log: Log):Long
    fun findById(logId: Long):Log?
}