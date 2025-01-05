package info.logbat.domain.project.presentation

import info.logbat.domain.project.application.AppService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/apps")
class AppController (
    private val appService:AppService
){
    @DeleteMapping("/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeAppCache(@PathVariable("token") token:String){
        appService.evictAppCache(token)
    }
}