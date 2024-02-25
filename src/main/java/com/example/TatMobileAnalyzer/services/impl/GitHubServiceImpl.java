package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.GitHubService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubServiceImpl implements GitHubService {

    @Value("${access.token}")
    private String accessToken;

    @SneakyThrows
    public HttpResponse<String> sendGetRequest(String apiUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                header("Authorization", "Bearer " + accessToken).
                uri(URI.create(apiUrl)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public List<Map<String, Object>> readJsonToList(String data) {
        List jsonData = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.readValue(data, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    public Map<String, Object> readJsonToMap(String data) {
        Map<String, Object> jsonData = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
}
