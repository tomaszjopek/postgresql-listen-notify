/* (C)2022 */
package pl.itj.dev.ssepostresql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ObjectMapperTest {

    private final ObjectMapper objectMapper;

    @Autowired
    public ObjectMapperTest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    public void shouldDeserializeCorrectZonedDateTime() throws JsonProcessingException {
        String dateText = "\"2022-11-24T20:46:48.808+00:00\"";

        ZonedDateTime result = objectMapper.readValue(dateText, ZonedDateTime.class);
        ZonedDateTime expectedObject =
                ZonedDateTime.of(2022, 11, 24, 20, 46, 48, 808000000, ZoneId.of("UTC"));
        assertEquals(expectedObject, result);
    }
}
