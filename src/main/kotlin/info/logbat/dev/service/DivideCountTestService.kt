package info.logbat.dev.service

import info.logbat.dev.util.ThreadLocalLongAdder
import org.springframework.stereotype.Component

@Component
class DivideCountTestService :CountTestService
{
    private val successCount: ThreadLocalLongAdder = ThreadLocalLongAdder()
    private val errorCount: ThreadLocalLongAdder = ThreadLocalLongAdder()

    override fun increaseSuccessCount() = successCount.increment()

    override fun increaseErrorCount() = errorCount.increment()

    override fun getSuccessCount(): Long = successCount.get()

    override fun getErrorCount(): Long  = errorCount.get()

    override fun reset() {
        successCount.reset()
        errorCount.reset()
    }
}