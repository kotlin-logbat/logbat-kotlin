package info.logbat.domain.project.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AppRepository (
    private val jdbcTemplate: JdbcTemplate
){
    fun getAppIdByToken(token: String): Long{
        val selectQuery = "SELECT id FROM apps WHERE app_key = UNHEX(REPLACE(?, '-', ''))"
        return jdbcTemplate.queryForObject(selectQuery, Long::class.java, token).or(0)
    }
}