package info.logbat.dev.presentation;

import info.logbat.common.payload.ApiCommonResponse;
import info.logbat.dev.presentation.payload.response.CountTestResponse;
import info.logbat.dev.service.CountTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/count")
@RequiredArgsConstructor
public class CountTestController {

  private final CountTestService countTestService;

  @GetMapping
  public ApiCommonResponse<CountTestResponse> count() {

    return ApiCommonResponse.createSuccessResponse(
        CountTestResponse.of(countTestService.getSuccessCount(), countTestService.getErrorCount()));
  }

  @PutMapping("/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reset() {
    countTestService.reset();
  }

}
