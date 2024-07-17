package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.FactoryGitService;
import com.example.TatMobileAnalyzer.services.GitService;
import com.example.TatMobileAnalyzer.services.impl.git.apis.GitHubService;
import com.example.TatMobileAnalyzer.services.impl.git.apis.GitLabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class FactoryGitServiceImpl implements FactoryGitService {

    private final GitHubService gitHubService;
    private final GitLabService gitLabService;

    private final Map<String, GitService> serviceMap = new ConcurrentHashMap<>();

    @Autowired
    public FactoryGitServiceImpl(GitHubService gitHubService, GitLabService gitLabService) {
        this.gitHubService = gitHubService;
        this.gitLabService = gitLabService;
    }

    @PostConstruct
    public void init() {
        serviceMap.put("github", gitHubService);
        serviceMap.put("gitlab", gitLabService);
    }

    @Override
    public GitService getImplementation(String repositoryUrl) {
        if (repositoryUrl == null || repositoryUrl.isEmpty()) {
            log.warn("Repository URL is not supported: {}", repositoryUrl);
            return null;
        }

        return serviceMap.entrySet().stream()
                .filter(entry -> repositoryUrl.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseGet(() -> {
                    log.warn("Repository URL is not supported: {}", repositoryUrl);
                    return null;
                });
    }
}
