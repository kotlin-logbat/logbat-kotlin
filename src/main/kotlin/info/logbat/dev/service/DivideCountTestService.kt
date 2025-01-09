package info.logbat.dev.service

import info.logbat.dev.util.ThreadLocalLongAdder
import org.springframework.stereotype.Component

@Component
class DivideCountTestService (
    private val successCount: ThreadLocalLongAdder = ThreadLocalLongAdder()
){
}