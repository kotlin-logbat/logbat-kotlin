package info.logbat.dev.presentation

import info.logbat.common.payload.ApiCommonResponse
import info.logbat.dev.presentation.payload.response.CountTestResponse
import info.logbat.dev.service.CountTestService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test/count")
class CountTestController (
    private val countTestService: CountTestService
){
    @GetMapping
    fun count():ApiCommonResponse<CountTestResponse>
            = ApiCommonResponse.createSuccessResponse(
                CountTestResponse.of(countTestService.getSuccessCount()
                    , countTestService.getErrorCount())
            )

    @PutMapping("/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reset() = countTestService.reset()
}