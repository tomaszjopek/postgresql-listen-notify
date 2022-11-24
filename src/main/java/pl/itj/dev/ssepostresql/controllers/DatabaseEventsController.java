package pl.itj.dev.ssepostresql.controllers;

import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itj.dev.ssepostresql.listener.AnnotationsListener;
import pl.itj.dev.ssepostresql.model.events.AnnotationEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@RestController
@CrossOrigin(origins = "*")
public class DatabaseEventsController {

    private final AnnotationsListener annotationsListener;

    public DatabaseEventsController(AnnotationsListener annotationsListener) {
        this.annotationsListener = annotationsListener;
    }

    @GetMapping(path = "/annotations-db-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AnnotationEvent> annotationsDbEvents() {
        return Flux.create(sink -> {
            MessageHandler handler = message -> sink.next((AnnotationEvent) message.getPayload());
            sink.onCancel(() -> annotationsListener.getSubscribableChannel().unsubscribe(handler));
            annotationsListener.getSubscribableChannel().subscribe(handler);
        }, FluxSink.OverflowStrategy.LATEST);
    }
}
