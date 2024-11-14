package org.example.olympicbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OlympicApplication {

    public static void main(String[] args) {
        SpringApplication.run(OlympicApplication.class, args);
    }

}
