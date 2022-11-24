package pl.itj.dev.ssepostresql.model.events;

import lombok.Data;
import lombok.ToString;
import pl.itj.dev.ssepostresql.model.Annotation;

import java.time.ZonedDateTime;

@Data
@ToString
public class AnnotationEvent {
    private ZonedDateTime timestamp;
    private Action action;
    private String schema;
    private String identity;
    private Annotation record;
    private Annotation old;
}