package com.example.mega.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void fetchAndLogResponse() {
        String url = "https://api.restful-api.dev/objects";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String prettyJson = objectMapper.readTree(response.getBody()).toPrettyString();
            log.info("Formatted JSON Response: \n{}", prettyJson);
        } catch (Exception e) {
            log.error("Error fetching data from API", e);
        }
    }
}
