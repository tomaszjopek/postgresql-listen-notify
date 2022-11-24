package pl.itj.dev.ssepostresql.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.itj.dev.ssepostresql.model.events.AnnotationEvent;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@Component
@Slf4j
public class AnnotationsListener {

    private final PGConnection connection;

    @Getter
    private final SubscribableChannel subscribableChannel = MessageChannels.publishSubscribe().get();

    private final ObjectMapper objectMapper;

    public AnnotationsListener(DataSource dataSource, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        try {
            var connection = dataSource.getConnection();
            var listenStatement = connection.createStatement();
            listenStatement.execute("LISTEN annotations_db_event");
            listenStatement.close();
            this.connection = connection.unwrap(PgConnection.class);
        } catch (SQLException e) {
            log.error("Could not initialize Annotations listener");
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void checkAnnotationsNotifications() {
        try {
            PGNotification[] notifications = connection.getNotifications();
            if (notifications.length > 0) {
                Arrays.stream(notifications)
                        .map(PGNotification::getParameter)
                        .map(this::deserializeDbEvent)
                        .peek(event -> log.debug("Notification received: {}", event.toString()))
                        .forEach(notification -> subscribableChannel.send(new GenericMessage<>(notification)));
            } else {
                log.debug("Notifications empty iteration");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AnnotationEvent deserializeDbEvent(String eventStr) {
        try {
            return objectMapper.readValue(eventStr, AnnotationEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize db event!");
            throw new RuntimeException(e);
        }
    }
}
