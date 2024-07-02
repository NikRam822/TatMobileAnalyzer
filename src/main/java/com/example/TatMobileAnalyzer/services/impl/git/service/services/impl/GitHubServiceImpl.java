package com.example.TatMobileAnalyzer.services.impl.git.service.services.impl;

import com.example.TatMobileAnalyzer.services.impl.git.service.IGitService;
import com.example.TatMobileAnalyzer.services.impl.git.service.services.GitHubService;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

@Service
@Qualifier("github")
@Slf4j
public class GitHubServiceImpl implements IGitService, GitHubService {
    @Value("${access.token.github}")
    private String accessToken;

    @SneakyThrows
    @Override
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

    @SneakyThrows
    @Override
    public boolean isValidRepository(String repositoryUrl) {
        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String owner = pathParts[1];
        String repositoryName = pathParts[2];
        try {
            GitHub github = GitHub.connectUsingOAuth(accessToken);
            github.getRepository(owner + "/" + repositoryName);
            log.info("Repository {} is valid", repositoryUrl);
            return true;
        } catch (IOException e) {
            log.warn("Repository not found or validation error with link: {}", repositoryUrl, e);
            return false;
        }
    }
}
