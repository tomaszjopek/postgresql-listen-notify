/* (C)2022 */
package pl.itj.dev.ssepostresql.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.itj.dev.ssepostresql.model.Annotation;

@Repository
public interface AnnotationsRepository extends CrudRepository<Annotation, String> {

    List<Annotation> findByText(String text);
}
