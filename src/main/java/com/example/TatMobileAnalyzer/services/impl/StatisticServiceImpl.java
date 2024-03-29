package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.services.StatisticService;
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
public class StatisticServiceImpl implements StatisticService {

    private final GitHubService gitHubService;

    @Autowired
    public StatisticServiceImpl(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }


    @Override
    public ResponseEntity getStatistic(String repoUrl) {
        String apiUrl = (repoUrl.substring(0, 8) + "api." + repoUrl.substring(8, 19) +
                "repos" + repoUrl.substring(18)).replaceAll("/$", "") +
                "/stats/contributors";

        HttpResponse<String> response = gitHubService.sendGetRequest(apiUrl);
        if (response.statusCode() != 200) {
            return new ResponseEntity(response.body(), HttpStatus.valueOf(response.statusCode()));
        }

        String stats = statisticHandler(response.body());

        return new ResponseEntity<>(stats, HttpStatus.valueOf(response.statusCode()));
    }

    private String statisticHandler(String data) {
        List<Map<String, Object>> contributorsData = gitHubService.readJsonToList(data);
        List<Map<String, Object>> contributorsStats = calculateContributorStats(contributorsData);
        return contributorsStats.toString();
    }


    private List<Map<String, Object>> calculateContributorStats(List<Map<String, Object>> contributorsData) {
        List<Map<String, Object>> contributorsStats = new ArrayList<>();
        int totalCommits = 0;
        int totalEdits = 0;

        for (Map<String, Object> contributor : contributorsData) {
            Map<String, Object> contributorStat = new HashMap<>();
            Map<String, String> author = (Map<String, String>) contributor.get("author");
            contributorStat.put("author", author.get("login"));
            contributorsStats.add(contributorStat);

            int commits = (int) contributor.get("total");
            contributorStat.put("commits", commits);

            int editions = 0;
            List<Map<String, Integer>> weeks = (List<Map<String, Integer>>) contributor.get("weeks");
            for (Map<String, Integer> week : weeks) {
                editions += week.get("a") + week.get("d");
            }
            contributorStat.put("editions", editions);

            totalCommits += commits;
            totalEdits += editions;
        }

        for (Map<String, Object> contributor : contributorsStats) {
            double commitsPercentage = ((int) contributor.get("commits") / (double) totalCommits) * 100;
            double editsPercentage = ((int) contributor.get("editions") / (double) totalEdits) * 100;

            contributor.put("contribution_percentage_by_commits", Math.round(commitsPercentage * 100) / 100.0);
            contributor.put("editions_percentage_by_commits", Math.round(editsPercentage * 100) / 100.0);
        }

        return contributorsStats;
    }
}
