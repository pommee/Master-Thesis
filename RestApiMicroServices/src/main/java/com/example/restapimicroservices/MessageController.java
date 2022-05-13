package com.example.restapimicroservices;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MessageController {

    @PostMapping("/api/message")
    public String retriveLimitsFromConfigurations(@RequestBody String test) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://localhost:8083/api/message/service", String.class);
        return result;
    }
}
