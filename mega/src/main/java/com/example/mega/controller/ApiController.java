package com.example.mega.controller;

import com.example.mega.service.impl.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch-api-data")
    public ResponseEntity<String> fetchApiData() {
        apiService.fetchAndLogResponse();
        return ResponseEntity.ok("Request sent to external API. Check logs for the response.");
    }
}