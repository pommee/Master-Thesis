package com.example.restapimonolith;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@RestController
public class Controller implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @RequestMapping(value = "/api/message", method = RequestMethod.POST, headers = "Accept=*/*", produces = "application/json", consumes = "application/json")
    public int message(@RequestBody String keyword) {
        return HttpServletResponse.SC_OK;
    }
}
