package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.FileStatService;
import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.utils.MapToCsvConverter;
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
public class FileStatServiceImpl implements FileStatService {

    private final GitHubService gitHubService;

    @Autowired
    public FileStatServiceImpl(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public ResponseEntity getContributorsByFiles(String repoUrl) {
        String apiUrl = constructApiUrl(repoUrl);

        HttpResponse<String> response = gitHubService.sendGetRequest(apiUrl);
        if (response.statusCode() != 200) {
            return new ResponseEntity(response.body(), HttpStatus.valueOf(response.statusCode()));
        }

//        Map<String, List<Object>> fileContributorsMap = processCommits(response.body());
        Map<String, List<Map<String, Object>>> fileContributorsMap = processCommits(response.body());

        String outputPath = "output.csv";
        MapToCsvConverter.mapToCsv(fileContributorsMap, outputPath);

        return new ResponseEntity<>(fileContributorsMap, HttpStatus.OK);
    }

    private String constructApiUrl(String repoUrl) {
        return (repoUrl.substring(0, 8) + "api." + repoUrl.substring(8, 19) +
                "repos" + repoUrl.substring(18)).replaceAll("/$", "") +
                "/commits";
    }

    private Map<String, List<Map<String, Object>>> processCommits(String responseData) {
        List<Map<String, Object>> commitData = gitHubService.readJsonToList(responseData);
        Map<String, List<Map<String, Object>>> fileContributorsMap = new HashMap<>();

        for (Map<String, Object> commit : commitData) {
            String commitUrl = (String) commit.get("url");
            String commitSHA = (String) commit.get("sha");

            Map<String, Object> author = (Map<String, Object>) commit.get("author");
            String authorLogin = String.valueOf(author.get("login"));

            Map<String, Object> commitMeta = (Map<String, Object>) commit.get("commit");
            Map<String, Object> commitAuthor = (Map<String, Object>) commitMeta.get("author");
            String commitDate = String.valueOf(commitAuthor.get("date"));

            HttpResponse<String> commitResponse = gitHubService.sendGetRequest(commitUrl);
            if (commitResponse.statusCode() == 200) {
                Map<String, Object> commitInfo = gitHubService.readJsonToMap(commitResponse.body());
                List<Map<String, Object>> filesChanged = (List<Map<String, Object>>) commitInfo.get("files");

                for (Map<String, Object> file : filesChanged) {
                    String filename = (String) file.get("filename");
                    Map<String, Object> fileStats = new HashMap<>();
                    fileStats.put("commitSHA", commitSHA);
                    fileStats.put("commitDate", commitDate);
                    fileStats.put("additions", file.get("additions"));
                    fileStats.put("deletions", file.get("deletions"));
                    fileStats.put("changes", file.get("changes"));

                    List<Map<String, Object>> contributors = fileContributorsMap.getOrDefault(filename, new ArrayList<Map<String, Object>>());
                    boolean contributorExists = false;
                    for (Object contributor : contributors) {
                        if (contributor instanceof Map) {
                            Map<String, Object> contributorInfo = (Map<String, Object>) contributor;
                            String contributorName = contributorInfo.keySet().iterator().next(); // Получаем имя автора из ключа Map
                            if (contributorName.equals(authorLogin)) {
                                contributorExists = true;
                                List<Map<String, Object>> changes = (List<Map<String, Object>>) contributorInfo.getOrDefault(authorLogin, new ArrayList<>());
                                changes.add(fileStats);
                                contributorInfo.put(authorLogin, changes);
                                break;
                            }
                        }
                    }
                    if (!contributorExists) {
                        Map<String, Object> newContributorInfo = new HashMap<>();
                        List<Map<String, Object>> changes = new ArrayList<>();
                        changes.add(fileStats);
                        newContributorInfo.put(authorLogin, changes);
                        contributors.add(newContributorInfo);
                    }

                    fileContributorsMap.put(filename, contributors);
                }
            }
        }
        return fileContributorsMap;
    }
}
