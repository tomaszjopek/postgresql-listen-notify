/* (C)2022 */
package pl.itj.dev.ssepostresql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SsePostresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsePostresqlApplication.class, args);
    }
}
