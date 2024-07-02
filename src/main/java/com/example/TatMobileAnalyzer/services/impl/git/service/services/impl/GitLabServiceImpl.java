package com.example.TatMobileAnalyzer.services.impl.git.service.services.impl;

import com.example.TatMobileAnalyzer.services.impl.git.service.IGitService;
import com.example.TatMobileAnalyzer.services.impl.git.service.services.GitLabService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Service("")
@Slf4j
public class GitLabServiceImpl implements IGitService, GitLabService {


    @Value("${access.token.gitlab}")
    private String accessToken;

    @Value("${gitlab.url}")
    private String gitLabUrl;

    @SneakyThrows
    @Override
    public List<Commit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until) {
        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);

        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String projectPath = pathParts[1] + "/" + pathParts[2];

        Project project = gitLabApi.getProjectApi().getProject(projectPath);
        List<Commit> commits = gitLabApi.getCommitsApi().getCommits(project.getId(), null, since, until);
        gitLabApi.close();
        return commits;
    }

    @SneakyThrows
    @Override
    public boolean isValidRepository(String repositoryUrl) {
        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);

        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String projectPath = pathParts[1] + "/" + pathParts[2];

        try {
            gitLabApi.getProjectApi().getProject(projectPath);
            log.info("Repository {} is valid", repositoryUrl);
            return true;
        } catch (Exception e) {
            log.warn("Repository not found or validation error with link: {}", repositoryUrl, e);
            return false;
        } finally {
            gitLabApi.close();
        }
    }
}

