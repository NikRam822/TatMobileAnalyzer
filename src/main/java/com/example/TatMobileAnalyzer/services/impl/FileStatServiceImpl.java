package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.FileStatService;
import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.utils.MapToCsvConverter;
import lombok.SneakyThrows;
import org.kohsuke.github.GHCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.*;

@Service
public class FileStatServiceImpl implements FileStatService {

    private final GitHubService gitHubService;

    @Autowired
    public FileStatServiceImpl(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public ResponseEntity getContributorsByFiles(String repoUrl, Date since, Date until) {

        List<GHCommit> commitsPerPeriod = gitHubService.getCommitsPerPeriod(repoUrl, since, until);

//        Map<String, List<Object>> fileContributorsMap = processCommits(response.body());
        Map<String, List<Map<String, Object>>> fileContributorsMap = processCommits(commitsPerPeriod);

        String outputPath = "output.csv";
        MapToCsvConverter.mapToCsv(fileContributorsMap, outputPath);

        return new ResponseEntity<>(fileContributorsMap, HttpStatus.OK);
    }

    @SneakyThrows
    private Map<String, List<Map<String, Object>>> processCommits(List<GHCommit> commitsPerPeriod) {
        Map<String, List<Map<String, Object>>> fileContributorsMap = new HashMap<>();

        for (GHCommit commit : commitsPerPeriod) {
            String author = commit.getCommitShortInfo().getAuthor().getName();
            List<GHCommit.File> files = commit.listFiles().toList();

            for (GHCommit.File file : files) {
                String filename = file.getFileName();
                Map<String, Object> fileStats = new HashMap<>();
                fileStats.put("commitSHA", commit.getSHA1());
                fileStats.put("commitDate", commit.getCommitDate().toString());
                fileStats.put("additions", file.getLinesAdded());
                fileStats.put("deletions", file.getLinesDeleted());
                fileStats.put("changes", file.getLinesChanged());

                List<Map<String, Object>> contributors = fileContributorsMap.getOrDefault(filename, new ArrayList<Map<String, Object>>());
                boolean contributorExists = false;
                for (Object contributor : contributors) {
                    if (contributor instanceof Map) {
                        Map<String, Object> contributorInfo = (Map<String, Object>) contributor;
                        String contributorName = contributorInfo.keySet().iterator().next(); // Get the author's name from the Map key
                        if (contributorName.equals(author )) {
                            contributorExists = true;
                            List<Map<String, Object>> changes = (List<Map<String, Object>>) contributorInfo.getOrDefault(author, new ArrayList<>());
                            changes.add(fileStats);
                            contributorInfo.put(author, changes);
                            break;
                        }
                    }
                }
                if (!contributorExists) {
                    Map<String, Object> newContributorInfo = new HashMap<>();
                    List<Map<String, Object>> changes = new ArrayList<>();
                    changes.add(fileStats);
                    newContributorInfo.put(author, changes);
                    contributors.add(newContributorInfo);
                }

                fileContributorsMap.put(filename, contributors);
            }
        }
        return fileContributorsMap;
    }
}
