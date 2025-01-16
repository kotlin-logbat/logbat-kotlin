package info.logbat.domain.log.presentation

import info.logbat.dev.aop.CountTest
import info.logbat.domain.log.application.LogService
import info.logbat.domain.log.presentation.payload.request.CreateLogRequest
import info.logbat.domain.log.repository.LogRepository
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/log")
class LogController (
    private val logService: LogService
){
    @CountTest
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveLogs(
        @RequestHeader("App-Key") @NotBlank(message = "appKey가 비어있습니다.") appKey:String,
        @RequestBody @NotEmpty requests: List<CreateLogRequest>
    ) {
        logService.saveLogs(appKey, requests)
    }
}