package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.GitHubService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {

    @Value("${access.token}")
    private String accessToken;

    @Override
    @SneakyThrows
    public List<GHCommit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until) {

        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String owner = pathParts[1];
        String repositoryName = pathParts[2];

        GitHub github = GitHub.connectUsingOAuth(accessToken);

        GHCommitQueryBuilder commitQueryBuilder = github.getRepository(owner + "/" + repositoryName).queryCommits();

        commitQueryBuilder = (since != null) ? commitQueryBuilder.since(since) : commitQueryBuilder;
        commitQueryBuilder = (until != null) ? commitQueryBuilder.until(until) : commitQueryBuilder;

        List<GHCommit> commitsPerPeriod = Lists.reverse(commitQueryBuilder.list().toList());
        return commitsPerPeriod;
    }
}
