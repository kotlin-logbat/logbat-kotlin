package info.logbat.domain.log.application;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@DisplayName("LogService는")
@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @InjectMocks
    private LogService2 logService;

    @Mock
    private LogRepository logRepository;
    @Mock
    private AppService_ASIS appService;

    private final LocalDateTime expectedTimestamp = LocalDateTime.of(2021, 1, 1, 0, 0, 1);
    private final List<CreateLogRequest> expectedCreateLogRequest = List.of(
        new CreateLogRequest("INFO", "data1", expectedTimestamp),
        new CreateLogRequest("ERROR", "data2", expectedTimestamp)
    );

    @Nested
    @DisplayName("로그들을 저장할 때")
    class whenSaveLogs {

        @Test
        @DisplayName("정상적으로 저장한다.")
        void willSave() {
            // Arrange
            Long expectedAppId = 1L;
            given(appService.getAppIdByToken(any(String.class))).willReturn(expectedAppId);
            // Act
            logService.saveLogs("appKey", expectedCreateLogRequest);
            // Assert
            verify(logRepository).saveAll(any());
        }

        @Test
        @DisplayName("앱 키로 앱을 찾을 수 없으면 예외를 던진다.")
        void willThrowExceptionWhenAppNotFound() {
            // Arrange
            given(appService.getAppIdByToken(any(String.class))).willThrow(
                new IllegalArgumentException("앱을 찾을 수 없습니다."));
            // Act & Assert
            assertThatThrownBy(() -> logService.saveLogs("appKey", expectedCreateLogRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("앱을 찾을 수 없습니다.");
        }

    }
}