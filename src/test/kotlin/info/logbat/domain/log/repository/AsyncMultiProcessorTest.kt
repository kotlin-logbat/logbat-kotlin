package info.logbat.domain.log.repository

import com.zaxxer.hikari.HikariDataSource
import info.logbat.domain.log.domain.Log
import info.logbat.domain.log.domain.enums.Level
import info.logbat.domain.log.domain.values.LogData
import info.logbat.domain.log.queue.ReentrantLogQueue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.ObjectProvider
import org.springframework.jdbc.core.JdbcTemplate
import org.assertj.core.api.Assertions.assertThat
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.jdbc.core.RowMapper
import java.time.LocalDateTime
import javax.sql.DataSource

class MySuspendMockFun{
    suspend fun suspendFun(list:List<Log>){
        println("mock test")
    }
}

@ExtendWith(MockitoExtension::class)
@DisplayName("AsyncMultiProcessorTest는")
class AsyncMultiProcessorTest{
//@Mock
//    private lateinit var asyncMultiProcessor: AsyncMultiProcessor<Log>
    @Spy
    private lateinit var jdbcTemplate: JdbcTemplate

    @Mock
    private lateinit var objectProvider: ObjectProvider<ReentrantLogQueue<Log>>

    private lateinit var asyncMultiProcessor: AsyncMultiProcessor<Log>

    private val expectedLog = Log(
        id = 1L,
        appId = 1L,
        level = Level.from(0),
        data = LogData.from("Test log data"),
        timestamp = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
    )

    private lateinit var expectedRetrant: ReentrantLogQueue<Log>

    @BeforeEach
    fun setUp() {
        val mockDataSource = mock(HikariDataSource::class.java)
        jdbcTemplate = spy(JdbcTemplate(mockDataSource))
        asyncMultiProcessor = AsyncMultiProcessor(3,5000,3000,jdbcTemplate, objectProvider)
    }

    @Test
    @DisplayName("잘 생성된다.")
    fun initTest(){
        var suspendFun = mock(MySuspendMockFun::class.java)
        given(objectProvider.getObject())
            .willReturn(expectedRetrant)
        assertThat(asyncMultiProcessor.init { suspendFun }).isNotNull
    }


}

