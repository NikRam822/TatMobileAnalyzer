package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.services.LocFilesService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocFilesServiceImpl implements LocFilesService {

    private final GitHubService gitHubService;

    @Autowired
    public LocFilesServiceImpl(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public ResponseEntity<String> getStatisticLocFiles(String repoUrl, String since, String until) {
        String apiUrl = (repoUrl.substring(0, 8) + "api." + repoUrl.substring(8, 19) +
                "repos" + repoUrl.substring(18)).replaceAll("/$", "") + "/commits" + since + until;
        HttpResponse<String> response = gitHubService.sendGetRequest(apiUrl);
        if (response.statusCode() != 200) {
            return new ResponseEntity(response.body(), HttpStatus.valueOf(response.statusCode()));
        }
        List<Map<String, Object>> commitsJson = gitHubService.readJsonToList(response.body());
        List<Map<String, Object>> result = getCommitsStatistic(commitsJson);
        return new ResponseEntity<>(result.toString(), HttpStatus.valueOf(response.statusCode()));
    }


    private List<Map<String, Object>> getCommitsStatistic(List<Map<String, Object>> commitsJson) {
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
        HttpResponse<String> response = gitHubService.sendGetRequest(commitUrl);
        Map<String, Object> filesJson = gitHubService.readJsonToMap(response.body());
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
