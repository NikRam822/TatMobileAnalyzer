package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.services.LocFilesService;
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
    public ResponseEntity<String> getStatisticLocFiles(String repoUrl, String period) {
        String apiUrl = (repoUrl.substring(0, 8) + "api." + repoUrl.substring(8, 19) +
                "repos" + repoUrl.substring(18)).replaceAll("/$", "") + "/commits" + period;
        HttpResponse<String> response = gitHubService.sendGetRequest(apiUrl);
        if (response.statusCode() != 200) {
            return new ResponseEntity(response.body(), HttpStatus.valueOf(response.statusCode()));
        }
        List<Map<String, Object>> commitsJson = gitHubService.readJsonToList(response.body());
        Map<String, List<Map<String, Object>>> result = getCommitsStatistic(commitsJson);
        return new ResponseEntity<>(result.toString(), HttpStatus.valueOf(response.statusCode()));
    }


    private Map<String, List<Map<String, Object>>> getCommitsStatistic(List<Map<String, Object>> commitsJson) {
        Map<String, List<Map<String, Object>>> authorData = new HashMap<>();

        for (Map<String, Object> commitJson : commitsJson) {
            Map<String, Object> author = (Map<String, Object>) commitJson.get("author");
            String authorLogin = String.valueOf(author.get("login"));
            List<Map<String, Object>> commits = authorData.getOrDefault(authorLogin, new ArrayList<>());

            Map<String, Object> commitData = new HashMap<>();
            List<Map<String, Object>> files = getFilesStatistic((String) commitJson.get("url"));
            commitData.put("sha", String.valueOf(commitJson.get("sha")));

            int totalAdditions = 0;
            int totalDeletions = 0;
            for (Map<String, Object> file : files) {
                totalAdditions += (int) file.get("add");
                totalDeletions += (int) file.get("del");
            }
            commitData.put("totalAdditions", totalAdditions);
            commitData.put("totalDeletions", totalDeletions);
            commitData.put("linesDifference", totalAdditions - totalDeletions);

            commitData.put("files", files);

            commits.add(commitData);
            authorData.put(authorLogin, commits);
        }

        return authorData;
    }


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
