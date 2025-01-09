package info.logbat.dev.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean

@Component
class MemoryUsageLogger {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(MemoryUsageLogger::class.java)
        val memoryMXBean: MemoryMXBean = ManagementFactory.getMemoryMXBean()
    }
    @Scheduled(fixedRate = 1000)
    fun logMemoryUsage() {
        val heapMemoryUsage = memoryMXBean.heapMemoryUsage
        val nonHeapMemoryUsage = memoryMXBean.nonHeapMemoryUsage
        logger.info("Memory Usage [Heap: Used={} MB, Max={} MB, Committed={} MB | Non-Heap: Used={} MB, Max={} MB, Committed={} MB | Pending Finalizations: {}]",
             heapMemoryUsage.used / 1024 / 1024,
             heapMemoryUsage.max / 1024 / 1024,
             heapMemoryUsage.committed / 1024 / 1024,
             nonHeapMemoryUsage.used / 1024 / 1024,
             nonHeapMemoryUsage.max / 1024 / 1024,
             nonHeapMemoryUsage.committed / 1024 / 1024,
             memoryMXBean.getObjectPendingFinalizationCount()
        )
    }
}