package info.logbat.domain.log.repository;

import info.logbat.domain.log.domain.Log;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("AsyncLogRepository는")
class AsyncLogRepositoryTest {

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private AsyncLogProcessor asyncLogProcessor;

    @Autowired
    private AsyncLogRepository asyncLogRepository;

    private final Long expectedLogId = 1L;
    private final Log expectedLog = new Log(expectedLogId, 1L, 0, "Test log data",
        LocalDateTime.of(2021, 1, 1, 0, 0, 0));

    @DisplayName("로그를 정상적으로 저장할 수 있다.")
    @Test
    void testSave() {
        // Act
        long actualResult = asyncLogRepository.save(expectedLog);

        // Assert
        assertThat(actualResult).isZero();
        verify(asyncLogProcessor).submitLog(expectedLog);
    }

    @Nested
    @DisplayName("로그 ID로 로그를 조회할 때")
    class whenFindLogById {

        @Test
        @DisplayName("로그가 존재하면 Optional에 로그를 반환한다.")
        void willReturnLogWithOptionalWhenLogExists() {
            // Arrange
            given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class),
                eq(expectedLogId))).willReturn(expectedLog);
            // Act
            Optional<Log> actualResult = asyncLogRepository.findById(expectedLogId);
            // Assert
            assertAll(
                () -> assertThat(actualResult).isPresent(),
                () -> assertThat(actualResult).get().isEqualTo(expectedLog)
            );
        }

        @Test
        @DisplayName("로그가 존재하지 않으면 빈 Optional을 반환한다.")
        void willReturnEmptyOptionalWhenLogDoesNotExist() {
            // Arrange
            given(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class),
                eq(expectedLogId))).willThrow(new EmptyResultDataAccessException(1));
            // Act
            Optional<Log> actualResult = asyncLogRepository.findById(expectedLogId);
            // Assert
            assertThat(actualResult).isEmpty();
        }
    }

}
