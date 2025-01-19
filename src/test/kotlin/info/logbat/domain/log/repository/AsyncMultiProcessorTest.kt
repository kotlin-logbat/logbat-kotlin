package info.logbat.domain.log.repository

import info.logbat.domain.log.domain.Log
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


    @BeforeEach
    fun setUp() {
        asyncMultiProcessor = AsyncMultiProcessor(3,5000,3000,jdbcTemplate, objectProvider)
    }

    @Test
    @DisplayName("잘 생성된다.")
    fun initTest(){
        val suspendFun = {data:List<Log> ->
            println(data)
        }
        assertThat(asyncMultiProcessor.init { suspendFun }).isNotNull
    }


}

