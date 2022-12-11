/* (C)2022 */
package pl.itj.dev.ssepostresql.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.itj.dev.ssepostresql.model.Annotation;
import pl.itj.dev.ssepostresql.model.events.AnnotationEvent;
import pl.itj.dev.ssepostresql.services.AnnotationService;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {DatabaseEventsControllerTest.Initializer.class})
@AutoConfigureWebTestClient(timeout = "10000")
class DatabaseEventsControllerTest {

    @Autowired private WebTestClient webTestClient;

    @Autowired private AnnotationService annotationService;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.1-alpine")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withInitScript("schema.sql");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                            "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                            "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void shouldReceiveEventAfterInsertOnAnnotationsTable() {
        Annotation annotation = new Annotation();
        annotation.setText("insertion");
        annotation.setUrl("http://insertion.test.com");
        annotationService.addAnnotation(annotation);

        List<ServerSentEvent<AnnotationEvent>> result =
                webTestClient
                        .get()
                        .uri("/annotations-db-events")
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(
                                new ParameterizedTypeReference<
                                        ServerSentEvent<AnnotationEvent>>() {})
                        .getResponseBody()
                        .take(1)
                        .collectList()
                        .block();

        assertEquals(1, result.size());
        assertEquals("http://insertion.test.com", result.get(0).data().getRecord().getUrl());
        assertEquals("insertion", result.get(0).data().getRecord().getText());
        assertNull(result.get(0).data().getOld());
        assertEquals("INSERT", result.get(0).data().getAction().toString());
    }
}
