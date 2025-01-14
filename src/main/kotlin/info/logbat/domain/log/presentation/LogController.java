package info.logbat.domain.log.presentation;

import info.logbat.dev.aop.CountTest;
import info.logbat.domain.log.application.LogService;
import info.logbat.domain.log.application.LogService2;
import info.logbat.domain.log.presentation.payload.request.CreateLogRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    public LogController(LogService logService) {
        this.logService = logService;
    }

    private final LogService logService;

    @CountTest
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLogs(
        @RequestHeader("App-Key") @NotBlank(message = "appKey가 비어있습니다.") String appKey,
        @RequestBody @NotEmpty List<CreateLogRequest> requests) {
        logService.saveLogs(appKey, requests);
    }

}

