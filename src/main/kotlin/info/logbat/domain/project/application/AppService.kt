package info.logbat.domain.project.application

import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = ["app"])
class AppService (

){
    //private static final String APP_NOT_FOUND_MESSAGE = "앱을 찾을 수 없습니다.";
    //    public static final String APP_EXISTS_MESSAGE = "앱이 존재합니다.";
}