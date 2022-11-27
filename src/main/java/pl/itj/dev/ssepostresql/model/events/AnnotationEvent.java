/* (C)2022 */
package pl.itj.dev.ssepostresql.model.events;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.ToString;
import pl.itj.dev.ssepostresql.model.Annotation;

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
