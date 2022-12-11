/* (C)2022 */
package pl.itj.dev.ssepostresql.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.itj.dev.ssepostresql.model.Annotation;
import pl.itj.dev.ssepostresql.repository.AnnotationsRepository;
import pl.itj.dev.ssepostresql.services.AnnotationService;

@Service
@Transactional
public class AnnotationServiceImpl implements AnnotationService {

    private final AnnotationsRepository annotationsRepository;

    public AnnotationServiceImpl(AnnotationsRepository annotationsRepository) {
        this.annotationsRepository = annotationsRepository;
    }

    @Override
    public Annotation addAnnotation(Annotation annotation) {
        return annotationsRepository.save(annotation);
    }

    @Override
    public Annotation updateAnnotation(Annotation annotation) {
        return annotationsRepository.save(annotation);
    }
}
