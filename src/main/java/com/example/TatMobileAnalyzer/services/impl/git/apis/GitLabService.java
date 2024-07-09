package com.example.TatMobileAnalyzer.services.impl.git.apis;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.services.GitService;
import com.example.TatMobileAnalyzer.services.impl.PatchReader;
import com.example.TatMobileAnalyzer.services.impl.churn.ChurnStat;
import com.example.TatMobileAnalyzer.services.impl.churn.SupportServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GitLabService implements GitService {


    @Value("${access.token.gitlab}")
    private String accessToken;

    @SneakyThrows
    @Override
    public List<Commit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until, String branch) {
        URI uri = new URI(repositoryUrl);
        String gitLabUrl = uri.getScheme() + "://" + uri.getHost();
        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);

        String[] pathParts = uri.getPath().split("/");
        String projectPath = pathParts[1] + "/" + pathParts[2];

        Project project = gitLabApi.getProjectApi().getProject(projectPath);

        if (branch == null || branch.isEmpty()) {
            branch = project.getDefaultBranch();
        }

        List<Commit> allCommits = gitLabApi.getCommitsApi().getCommits(project.getId(), branch, since, until);
        List<Commit> nonMergeCommits = new ArrayList<>();

        for (Commit commit : allCommits) {
            if (commit.getParentIds().size() <= 1) {
                nonMergeCommits.add(commit);
            }
        }

        gitLabApi.close();

        for(Commit commit : nonMergeCommits){
            System.out.println(commit.getMessage());
        }

        System.out.println(allCommits.size());

        System.out.println(nonMergeCommits.size());
        return Lists.reverse(nonMergeCommits);
    }


    @SneakyThrows
    @Override
    public boolean isValidRepository(String repositoryUrl) {
        URI uri = new URI(repositoryUrl);
        String gitLabUrl = uri.getScheme() + "://" + uri.getHost();
        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);

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

    @SneakyThrows
    @Override
    public List<String> getBranches(String repositoryUrl) {

        URI uri = new URI(repositoryUrl);
        String gitLabUrl = uri.getScheme() + "://" + uri.getHost();
        String[] pathParts = uri.getPath().split("/");
        String namespace = pathParts[1];
        String projectName = pathParts[2];

        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);
        String projectPath = namespace + "/" + projectName;
        List<Branch> branches = gitLabApi.getRepositoryApi().getBranches(projectPath);
        gitLabApi.close();

        return branches.stream()
                .map(Branch::getName)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void processCommits(List<?> commitsPerPeriod, ChurnStat churnStat, Long projectId, SupportServices supportServices) {
        for (Commit commit : (List<Commit>) commitsPerPeriod) {
            String author = commit.getAuthorName();
            List<Map<String, JsonNode>> authorCommits = churnStat.authorStats().getOrDefault(author, new ArrayList<>());

            List<Map<String, JsonNode>> fileStats = processCommitFiles(commit, churnStat, author, projectId, supportServices);
            Map<String, JsonNode> commitStat = new HashMap<>();

            String repositoryUrl = supportServices.projectService().getProjectById(projectId).getProjectLink();

            List<Diff> diffs = getDiffs(repositoryUrl, commit);
            int additions = 0;
            int deletions = 0;
            for (Diff diff : diffs) {
                String[] lines = diff.getDiff().split("\n");
                for (String line : lines) {
                    if (line.startsWith("+") && !line.startsWith("+++")) {
                        additions++;
                    } else if (line.startsWith("-") && !line.startsWith("---")) {
                        deletions++;
                    }
                }
            }

            commitStat.put("sha", supportServices.objectMapper().valueToTree(commit.getId()));
            commitStat.put("date", supportServices.objectMapper().valueToTree(commit.getCommittedDate().toString()));
            commitStat.put("linesDifference", supportServices.objectMapper().valueToTree(additions + deletions));
            commitStat.put("totalAdditions", supportServices.objectMapper().valueToTree(additions));
            commitStat.put("totalDeletions", supportServices.objectMapper().valueToTree(deletions));
            commitStat.put("files", supportServices.objectMapper().valueToTree(fileStats));

            authorCommits.add(commitStat);
            churnStat.authorStats().put(author, authorCommits);
        }
    }

    @SneakyThrows
    private List<Map<String, JsonNode>> processCommitFiles(Commit commit, ChurnStat churnStat, String author, Long projectId, SupportServices supportServices) {
        List<Map<String, JsonNode>> fileStats = new ArrayList<>();

        String repositoryUrl = supportServices.projectService().getProjectById(projectId).getProjectLink();

        List<Diff> diffs = getDiffs(repositoryUrl, commit);
        Filter filter = supportServices.filterService().getFiltersByProjectId(projectId);

        if (filter == null) {
            filter = new Filter();
        }

        for (Diff diff : diffs) {
            boolean matchesGenerated = filter.getGenerated().stream().anyMatch(diff.getNewPath()::startsWith);
            boolean matchesTest = filter.getTest().stream().anyMatch(diff.getNewPath()::startsWith);
            if (matchesGenerated || matchesTest) {
                continue;
            }

            PatchReader.PatchInfo patchInfo = PatchReader.readPatch(diff.getDiff());

            Map<String, JsonNode> fileStat = new HashMap<>();
            fileStat.put("filename", supportServices.objectMapper().valueToTree(diff.getNewPath()));
            fileStat.put("add all", supportServices.objectMapper().valueToTree(patchInfo.getAdd().size()));
            fileStat.put("del all", supportServices.objectMapper().valueToTree(patchInfo.getDel().size()));
            fileStats.add(fileStat);

            churnStat.overall().put(author, churnStat.overall().getOrDefault(author, 0) + patchInfo.getAdd().size());

            updateGeneralStatistics(diff, patchInfo, churnStat, author);
        }
        return fileStats;
    }


    @SneakyThrows
    private void updateGeneralStatistics(Diff diff, PatchReader.PatchInfo patchInfo, ChurnStat churnStat, String author) {
        Map<Integer, String> fileLines = churnStat.general().getOrDefault(diff.getNewPath(), new HashMap<>());
        for (String addedLine : patchInfo.getAdd()) {
            int lineNumber = Integer.parseInt(addedLine.split(". ")[0]);
            fileLines.put(lineNumber, author);
        }
        churnStat.general().put(diff.getNewPath(), fileLines);
    }

    @SneakyThrows
    private List<Diff> getDiffs(String repositoryUrl, Commit commit) {
        URI uri = new URI(repositoryUrl);
        String gitLabUrl = uri.getScheme() + "://" + uri.getHost();
        String[] pathParts = uri.getPath().split("/");
        String projectPath = pathParts[1] + "/" + pathParts[2];

        GitLabApi gitLabApi = new GitLabApi(gitLabUrl, accessToken);
        org.gitlab4j.api.models.Project gitLabProject = gitLabApi.getProjectApi().getProject(projectPath);

        List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(gitLabProject.getId(), commit.getId());
        gitLabApi.close();

        return diffs;
    }
}

