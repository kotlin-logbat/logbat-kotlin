package info.logbat.domain.log.repository

import info.logbat.domain.log.domain.Log
import info.logbat.domain.log.domain.enums.Level
import info.logbat.domain.log.domain.values.LogData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@DisplayName("AsyncLogRepository는")
class AsyncLogRepositoryTest {

    @Spy
    private lateinit var jdbcTemplate: JdbcTemplate

    @Mock
    private lateinit var asyncLogProcessor: AsyncLogProcessor

    @Mock
    private lateinit var asyncMultiProcessor: AsyncMultiProcessor<Log>

    private lateinit var asyncLogRepository: AsyncLogRepository

    private val expectedLogId: Long = 1L
    private val expectedLog = Log(
        id = expectedLogId,
        appId = 1L,
        level = Level.from(0),
        data = LogData.from("Test log data"),
        timestamp = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
    )

    @BeforeEach
    fun setUp() {
        asyncLogRepository = AsyncLogRepository(jdbcTemplate, asyncMultiProcessor)
    }

    @Test
    @DisplayName("로그를 정상적으로 저장할 수 있다.")
    fun testSave() {
        // Arrange
        doNothing().`when`(asyncMultiProcessor).submitLog(expectedLog)

        // Act
        val actualResult = asyncLogRepository.save(expectedLog)

        // Assert
        assertThat(actualResult).isZero()
        verify(asyncMultiProcessor).submitLog(expectedLog)
    }

    @Nested
    @DisplayName("로그 ID로 로그를 조회할 때")
    inner class WhenFindLogById {
        @Test
        @DisplayName("로그가 존재하면 Optional에 로그를 반환한다.")
        fun willReturnLogWithOptionalWhenLogExists() {
            // Arrange
            given(jdbcTemplate.queryForObject(anyString(), any<RowMapper<Log>>(), eq(expectedLogId)))
                .willReturn(expectedLog)

            // Act
            val actualResult = asyncLogRepository.findById(expectedLogId)

            // Assert
            assertAll(
                { assertThat(actualResult).isNotNull },
                { assertThat(actualResult).isEqualTo(expectedLog) }
            )
        }

        @Test
        @DisplayName("로그가 존재하지 않으면 빈 Optional을 반환한다.")
        fun willReturnEmptyOptionalWhenLogDoesNotExist() {
            // Arrange
            given(jdbcTemplate.queryForObject(anyString(), any<RowMapper<Log>>(), eq(expectedLogId)))
                .willThrow(EmptyResultDataAccessException(1))

            // Act
            val actualResult = asyncLogRepository.findById(expectedLogId)

            // Assert
            assertThat(actualResult).isNull()
        }
    }
}