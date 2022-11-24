package pl.itj.dev.ssepostresql.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ANNOTATIONS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String url;

    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Annotation that = (Annotation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
