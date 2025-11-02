package com.company.mongoDb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping("/check-api")
    public String apihandlerRequest(){
        return "It is Working fine...";
    }
}
