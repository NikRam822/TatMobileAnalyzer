package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.PatchScanService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Service
public class PatchScanServiceImpl implements PatchScanService {

    @Value("${access.token}")
    private String accessToken;

    @SneakyThrows
    @Override
    public ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, Date since, Date until) {
        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String owner = pathParts[1];
        String repositoryName = pathParts[2];


        GitHub github = GitHub.connectUsingOAuth(accessToken);

        GHCommitQueryBuilder commitQueryBuilder = github.getRepository(owner + "/" + repositoryName).queryCommits();


        commitQueryBuilder = (since != null) ? commitQueryBuilder.since(since) : commitQueryBuilder;

        commitQueryBuilder = (until != null) ? commitQueryBuilder.until(until) : commitQueryBuilder;


        List<GHCommit> commitsPerPeriod = commitQueryBuilder.list().toList();

        commitsPerPeriod = Lists.reverse(commitsPerPeriod);

        for (GHCommit commit : commitsPerPeriod) {
            System.out.println(commit.getCommitDate());

            List<GHCommit.File> files = commit.listFiles().toList();

            for (GHCommit.File file : files) {
                System.out.println(file.getFileName() + ": " );
            }
        }
        return null;
    }


}
