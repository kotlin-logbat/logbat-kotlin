package info.logbat.domain.log.flatter;

import info.logbat.domain.log.domain.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class LogRequestFlatter {

    private final EventProducer<Log> eventProducer;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public void flatten(List<Log> logs) {
        executor.submit(() -> eventProducer.produce(logs));
    }
}
