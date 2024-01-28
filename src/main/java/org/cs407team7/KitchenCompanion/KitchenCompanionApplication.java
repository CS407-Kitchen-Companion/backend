package org.cs407team7.KitchenCompanion;

import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableJpaAuditing
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class KitchenCompanionApplication {

    public static void main(String[] args) {

        SpringApplication.run(KitchenCompanionApplication.class, args);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> test_p() {
        return ResponseEntity.status(200).body(new GenericResponse("Hello World!"));
    }

}
