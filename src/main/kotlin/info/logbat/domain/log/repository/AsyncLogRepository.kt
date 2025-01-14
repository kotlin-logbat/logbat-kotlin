package info.logbat.domain.log.repository

import info.logbat.domain.log.domain.Log
import info.logbat.domain.log.domain.enums.Level
import info.logbat.domain.log.domain.values.LogData
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Timestamp

@Primary
@Repository
class AsyncLogRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val asyncMultiProcessor : AsyncMultiProcessor<Log>
) :LogRepository{
    private var DEFAULT_RETURNS = 0L
    private val log = LoggerFactory.getLogger(AsyncLogRepository::class.java)

    @PostConstruct
    fun init(){
        log.info("AsyncLogRepository is initialized.")
        asyncMultiProcessor.init(this::saveLogsToDatabase)
    }

    override fun save(log: Log): Long {
        return DEFAULT_RETURNS
    }

    override fun findById(logId: Long): Log? {
        val sql = "SELECT * FROM logs WHERE id = ?"
        return try {
            jdbcTemplate.queryForObject(sql, logRowMapper, logId)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    private fun saveLogsToDatabase(logs: List<Log>) {
        if (logs.isEmpty()) return

        val sql = buildString {
            append("INSERT INTO logs (app_id, level, data, timestamp) VALUES ")
            logs.forEachIndexed { index, _ ->
                append("(?, ?, ?, ?)")
                if (index < logs.size - 1) append(", ")
            }
        }

        jdbcTemplate.update(sql) { ps ->
            logs.flatMap { log ->
                listOf(
                    log.appId,
                    log.level.ordinal,
                    log.data.value,
                    Timestamp.valueOf(log.timestamp)
                )
            }.forEachIndexed { index, value ->
                when (value) {
                    is Long -> ps.setLong(index + 1, value)
                    is Int -> ps.setInt(index + 1, value)
                    is String -> ps.setString(index + 1, value)
                    is Timestamp -> ps.setTimestamp(index + 1, value)
                }
            }
        }
    }

    private val logRowMapper: (ResultSet, Int) -> Log = { rs, _ ->
        Log(
            id = rs.getLong("log_id"),
            appId = rs.getLong("app_id"),
            level = Level.from(rs.getInt("level")),
            data = LogData.from(rs.getString("data")),
            timestamp = rs.getTimestamp("timestamp").toLocalDateTime()
        )
    }

}