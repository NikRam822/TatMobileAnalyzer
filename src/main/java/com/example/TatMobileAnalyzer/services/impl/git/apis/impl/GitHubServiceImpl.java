package com.example.TatMobileAnalyzer.services.impl.git.apis.impl;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.services.GitService;
import com.example.TatMobileAnalyzer.services.impl.PatchReader;
import com.example.TatMobileAnalyzer.services.impl.churn.ChurnStat;
import com.example.TatMobileAnalyzer.services.impl.churn.SupportServices;
import com.example.TatMobileAnalyzer.services.impl.git.apis.GitHubService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
@Qualifier("github")
@Slf4j
public class GitHubServiceImpl implements GitService, GitHubService {
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

    @SneakyThrows
    @Override
    public List<String> getBranches(String repositoryUrl) {
        URI uri = new URI(repositoryUrl);
        String[] pathParts = uri.getPath().split("/");
        String owner = pathParts[1];
        String repositoryName = pathParts[2];

        GitHub github = GitHub.connectUsingOAuth(accessToken);
        GHRepository repository = github.getRepository(owner + "/" + repositoryName);

        return new ArrayList<>(repository.getBranches().keySet());
    }

    @Override
    @SneakyThrows
    public void processCommits(List<?> commitsPerPeriod, ChurnStat churnStat, Long projectId, SupportServices supportServices) {
        for (GHCommit commit : (List<GHCommit>) commitsPerPeriod) {
            String author = commit.getCommitShortInfo().getAuthor().getName();
            List<Map<String, JsonNode>> authorCommits = churnStat.authorStats().getOrDefault(author, new ArrayList<>());

            List<Map<String, JsonNode>> fileStats = processCommitFiles(commit, churnStat, author, projectId, supportServices);
            Map<String, JsonNode> commitStat = new HashMap<>();
            commitStat.put("sha", supportServices.objectMapper().valueToTree(commit.getSHA1()));
            commitStat.put("date", supportServices.objectMapper().valueToTree(commit.getCommitDate().toString()));
            commitStat.put("linesDifference", supportServices.objectMapper().valueToTree(commit.getLinesChanged()));
            commitStat.put("totalAdditions", supportServices.objectMapper().valueToTree(commit.getLinesAdded()));
            commitStat.put("totalDeletions", supportServices.objectMapper().valueToTree(commit.getLinesDeleted()));
            commitStat.put("files", supportServices.objectMapper().valueToTree(fileStats));

            authorCommits.add(commitStat);
            churnStat.authorStats().put(author, authorCommits);
        }

    }

    @SneakyThrows
    private List<Map<String, JsonNode>> processCommitFiles(GHCommit commit, ChurnStat churnStat, String author, Long projectId, SupportServices supportServices) {
        List<Map<String, JsonNode>> fileStats = new ArrayList<>();
        List<GHCommit.File> files = commit.listFiles().toList();
        Filter filter = supportServices.filterService().getFiltersByProjectId(projectId);

        if (filter == null) {
            filter = new Filter();
        }
        for (GHCommit.File file : files) {
            // Check if the file name begins with one of the lines in the filter.getGenerated() or filter.getTest() arrays
            boolean matchesGenerated = filter.getGenerated().stream().anyMatch(file.getFileName()::startsWith);
            boolean matchesTest = filter.getTest().stream().anyMatch(file.getFileName()::startsWith);
            if (matchesGenerated || matchesTest) {
                continue;
            }
            String patch = file.getPatch();
            PatchReader.PatchInfo patchInfo = PatchReader.readPatch(patch);

            Map<String, JsonNode> fileStat = new HashMap<>();
            fileStat.put("filename", supportServices.objectMapper().valueToTree(file.getFileName()));
            fileStat.put("add all", supportServices.objectMapper().valueToTree(file.getLinesAdded()));
            fileStat.put("del all", supportServices.objectMapper().valueToTree(file.getLinesDeleted()));
            //fileStat.put("path", file.getPatch());
            // fileStat.put("add", patchInfo.getAdd());
            // fileStat.put("del", patchInfo.getDel());
            fileStats.add(fileStat);
            churnStat.overall().put(author, churnStat.overall().getOrDefault(author, 0) + patchInfo.getAdd().size());

            updateGeneralStatistics(file, patchInfo, churnStat, author);
        }
        return fileStats;
    }

    @SneakyThrows
    private void updateGeneralStatistics(GHCommit.File file, PatchReader.PatchInfo patchInfo, ChurnStat churnStat, String author) {
        Map<Integer, String> fileLines = churnStat.general().getOrDefault(file.getFileName(), new HashMap<>());
        for (String addedLine : patchInfo.getAdd()) {
            int lineNumber = Integer.parseInt(addedLine.split(". ")[0]);
            fileLines.put(lineNumber, author);
        }
        churnStat.general().put(file.getFileName(), fileLines);
    }

}
