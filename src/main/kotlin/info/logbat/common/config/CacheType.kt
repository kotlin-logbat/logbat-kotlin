package info.logbat.common.config

import java.util.concurrent.TimeUnit

enum class CacheType(val cacheName: String, val expireAfterWrite: Long, val timeUnit: TimeUnit, val maximumSize: Long) {
    ELEMENTS(
        "app", // 캐시 이름
        10, // 캐시가 write 된 시점으로부터 만료 시간(초 단위)
        TimeUnit.MINUTES,
        100 // 최대 사이즈
    )
}
