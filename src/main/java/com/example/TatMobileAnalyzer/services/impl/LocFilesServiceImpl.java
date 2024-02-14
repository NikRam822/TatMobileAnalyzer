package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.LocFilesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class LocFilesServiceImpl implements LocFilesService {
    @Value("${access.token}")
    private String accessToken;

    @Override
    public ResponseEntity<String> getStatisticLocFiles(String repoUrl, String since, String until) {
        HttpResponse<String> response = getCommitsPerTime(repoUrl, since, until);
        if (response.statusCode() != 200) {
            return new ResponseEntity(response.body(), HttpStatus.valueOf(response.statusCode()));
        }
        List<Map<String, Object>> commitsJson = readJson(response.body());
        List<Map<String, Object>> result = getCommitsStatisics(commitsJson);
        return new ResponseEntity<>(result.toString(), HttpStatus.valueOf(response.statusCode()));
    }

    @SneakyThrows
    private HttpResponse<String> getCommitsPerTime(String repoUrl, String since, String until) {
        String apiUrl = (repoUrl.substring(0, 8) + "api." + repoUrl.substring(8, 19) +
                "repos" + repoUrl.substring(18)).replaceAll("/$", "") + "/commits?" + since + "&" + until;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", "Bearer " + accessToken).uri(URI.create(apiUrl)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private List<Map<String, Object>> readJson(String data) {
        List contributorsData = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            contributorsData = objectMapper.readValue(data, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contributorsData;
    }

    private Map<String, Object> readJsonToMap(String data) {
        Map<String, Object> contributorsData = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            contributorsData = objectMapper.readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contributorsData;
    }

    private List<Map<String, Object>> getCommitsStatisics(List<Map<String, Object>> commitsJson) {
        List<Map<String, Object>> commitsData = new ArrayList<>();
        for (Map<String, Object> commitJson : commitsJson) {
            Map<String, Object> commitStatData = new HashMap<>();
            commitStatData.put("sha", commitJson.get("sha"));
            Map<String, Object> author = (Map<String, Object>) commitJson.get("author");
            commitStatData.put("author", author.get("login"));
            List<Map<String, Object>> files = getFilesStatistic((String) commitJson.get("url"));
            commitStatData.put("files", files);
            commitsData.add(commitStatData);
        }
        return commitsData;

    }

    @SneakyThrows
    private List<Map<String, Object>> getFilesStatistic(String commitUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().header("Authorization", "Bearer " + accessToken).uri(URI.create(commitUrl)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> filesJson = readJsonToMap(response.body());
        List<Map<String, Object>> files = (List<Map<String, Object>>) filesJson.get("files");
        List<Map<String, Object>> resultJson = new ArrayList<>();
        for (Map<String, Object> file : files) {
            Map<String, Object> resultFile = new HashMap<>();
            resultFile.put("filename", file.get("filename"));
            resultFile.put("add", file.get("additions"));
            resultFile.put("del", file.get("deletions"));
            resultJson.add(resultFile);
        }
        return resultJson;
    }
}
