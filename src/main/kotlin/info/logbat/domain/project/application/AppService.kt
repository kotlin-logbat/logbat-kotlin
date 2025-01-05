package info.logbat.domain.project.application

import info.logbat.domain.project.repository.AppRepository
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = ["app"])
class AppService (
    private val appRepository:AppRepository
){
    val APP_NOT_FOUND_MESSAGE = "앱을 찾을 수 없습니다."
    val APP_EXISTS_MESSAGE = "앱이 존재합니다."

    @Cacheable(key = "#token")
    fun getAppIdByToken(token: String):Long{
        return appRepository.getAppIdByToken(token) ?:
            throw IllegalArgumentException(APP_NOT_FOUND_MESSAGE)
    }

    @CacheEvict(key = "#token")
    fun evictAppCache(token: String){
        appRepository.getAppIdByToken(token)?.let{
            throw IllegalStateException(APP_EXISTS_MESSAGE)
        }
    }
}