package com.example.greenshadow.controlller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/health")
public class HealthCheckController {

    @GetMapping
    public String healthTest(){
        return "Green Shadow API is working";
    }
}
