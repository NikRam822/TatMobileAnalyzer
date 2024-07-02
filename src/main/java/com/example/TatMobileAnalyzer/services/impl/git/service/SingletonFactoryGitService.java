package com.example.TatMobileAnalyzer.services.impl.git.service;

import com.example.TatMobileAnalyzer.services.impl.git.service.services.GitHubService;
import com.example.TatMobileAnalyzer.services.impl.git.service.services.GitLabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class SingletonFactoryGitService {

    private final GitHubService gitHubService;
    private final GitLabService gitLabService;

    private static SingletonFactoryGitService instance;

    @Autowired
    public SingletonFactoryGitService(GitHubService gitHubService, GitLabService gitLabService) {
        this.gitHubService = gitHubService;
        this.gitLabService = gitLabService;
    }

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static synchronized SingletonFactoryGitService getInstance() {

        return instance;
    }


    public IGitService getImplementation(String repositoryUrl) {

        if (repositoryUrl == null || repositoryUrl.isEmpty()) {
            log.warn("Repository URL is not supported: {}", repositoryUrl);
            return null;
        }

        if (repositoryUrl.contains("github.com")) {
            return (IGitService) gitHubService;
        } else if (repositoryUrl.contains("gitlab.com")) {
            return (IGitService) gitLabService;
        } else {
            log.warn("Repository URL is not supported: {}", repositoryUrl);
            return null;
        }
    }


}
