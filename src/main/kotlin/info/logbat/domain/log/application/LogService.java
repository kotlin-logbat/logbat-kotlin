package info.logbat.domain.log.application;

import info.logbat.common.event.EventProducer;
import info.logbat.domain.log.domain.Log;
import info.logbat.domain.log.presentation.payload.request.CreateLogRequest;
import info.logbat.domain.project.application.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private final EventProducer<Log> producer;
    private final AppService appService;

    private static final Logger log = LoggerFactory.getLogger(LogService2.class);

    public LogService(EventProducer<Log> producer, AppService appService) {
        this.producer = producer;
        this.appService = appService;
    }

    public void saveLogs(String appKey, List<CreateLogRequest> requests) {
        Long appId = appService.getAppIdByToken(appKey);
        List<Log> logs = new ArrayList<>(requests.size());
        requests.forEach(request -> {
            try {
                logs.add(request.toEntity(appId));
            } catch (Exception e) {
                log.error("Failed to convert request to entity: {}", request, e);
            }
        });
        producer.produce(logs);
    }

}
