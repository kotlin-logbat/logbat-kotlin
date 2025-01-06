package info.logbat.common.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CachingConfiguration {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()
        val caches = CacheType.entries.map {
            CaffeineCache(
                it.cacheName,
                Caffeine.newBuilder()
                    .expireAfterWrite(it.expireAfterWrite, it.timeUnit)
                    .maximumSize(it.maximumSize)
                    .build()
            )
        }
        cacheManager.setCaches(caches)
        return cacheManager
    }
}