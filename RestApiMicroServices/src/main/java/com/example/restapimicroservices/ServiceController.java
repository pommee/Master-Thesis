package com.example.restapimicroservices;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ServiceController {


    @GetMapping("/api/message/service")
    public int retriveLimitsFromConfigurations() {
        return HttpServletResponse.SC_OK;
    }
}
