package com.example.restapimicroservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@SpringBootApplication
public class RestApiMicroservicesApplicationService {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RestApiMicroservicesApplicationService.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8083"));
        app.run(args);
    }
}

