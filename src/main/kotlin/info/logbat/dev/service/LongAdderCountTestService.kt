package info.logbat.dev.service

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.LongAdder

@Primary
@Component
class LongAdderCountTestService : CountTestService{
    private val successCount = LongAdder()
    private val errorCount = LongAdder()


    override fun increaseSuccessCount()  = successCount.increment()

    override fun increaseErrorCount() = errorCount.increment()

    override fun getSuccessCount(): Long = successCount.sum()

    override fun getErrorCount(): Long = errorCount.sum();

    override fun reset() {
        successCount.reset()
        errorCount.reset()
    }
}