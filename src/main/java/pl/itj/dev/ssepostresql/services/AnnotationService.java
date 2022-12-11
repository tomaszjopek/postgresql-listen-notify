/* (C)2022 */
package pl.itj.dev.ssepostresql.services;

import pl.itj.dev.ssepostresql.model.Annotation;

public interface AnnotationService {

    Annotation addAnnotation(Annotation annotation);

    Annotation updateAnnotation(Annotation annotation);
}
