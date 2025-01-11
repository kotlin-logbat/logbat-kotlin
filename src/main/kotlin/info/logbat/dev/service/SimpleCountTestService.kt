package info.logbat.dev.service

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class SimpleCountTestService : CountTestService{
    private val successCount = AtomicLong(0L)
    private val errorCount = AtomicLong(0L)

    override fun increaseSuccessCount()  {
        successCount.incrementAndGet()
    }

    override fun increaseErrorCount() {
        errorCount.incrementAndGet()
    }

    override fun getSuccessCount(): Long = successCount.get()

    override fun getErrorCount(): Long = errorCount.get();

    override fun reset() {
        successCount.set(0)
        errorCount.set(0)
    }
}