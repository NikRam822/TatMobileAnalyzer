package com.example.TatMobileAnalyzer.services.impl.churn;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.services.ChurnService;
import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.impl.git.service.services.GitHubService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import com.example.TatMobileAnalyzer.services.impl.PatchReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ChurnServiceImpl implements ChurnService {

    FilterService filterService;
    GitHubService gitHubService;
    ObjectMapper objectMapper;
    ProjectService projectService;

    @Autowired
    public ChurnServiceImpl(FilterService filterService,
                            GitHubService gitHubService,
                            ObjectMapper objectMapper,
                            ProjectService projectService) {
        this.filterService = filterService;
        this.gitHubService = gitHubService;
        this.objectMapper = objectMapper;
        this.projectService = projectService;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getStatisticChurn(Date since, Date until, Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            log.warn("Project with id {} not found", projectId);
            return null;
        }
        //TODO: проверяем hub или lab, создаем processor(hub или lab реалезации и вызываем все методы)
        List<GHCommit> commitsPerPeriod = (List<GHCommit>)gitHubService.getCommitsPerPeriod(project.getProjectLink(), since, until);

        Map<String, List<Map<String, JsonNode>>> authorStats = new HashMap<>();
        Map<String, Integer> overall = new HashMap<>();
        Map<String, Map<Integer, String>> general = new HashMap<>();
        Map<String, Map<String, Integer>> generalResult = new HashMap<>();
        Map<String, Double> churn = new HashMap<>();

        ChurnStat churnStat = new ChurnStat(authorStats, overall, general, generalResult, churn);

        // TODO: создать обьект CommitProcessor и в нем реалезую processCommits
        processCommits(commitsPerPeriod, churnStat, projectId);

        // Create a map to hold the JSON nodes
        Map<String, Object> finalStats = new HashMap<>();
        finalStats.put("authorStats", churnStat.authorStats());
        finalStats.put("overall", churnStat.overall());
        finalStats.put("general", churnStat.generalResult());
        finalStats.put("churn", churnStat.churn());

        // Return ResponseEntity with the map of JSON nodes
        return finalStats;
    }


    @SneakyThrows
    private void processCommits(List<GHCommit> commitsPerPeriod, ChurnStat churnStat, Long projectId) {
        for (GHCommit commit : commitsPerPeriod) {
            String author = commit.getCommitShortInfo().getAuthor().getName();
            List<Map<String, JsonNode>> authorCommits = churnStat.authorStats().getOrDefault(author, new ArrayList<>());

            List<Map<String, JsonNode>> fileStats = processCommitFiles(commit, churnStat, author, projectId);

            Map<String, JsonNode> commitStat = new HashMap<>();
            commitStat.put("sha", objectMapper.valueToTree(commit.getSHA1()));
            commitStat.put("date", objectMapper.valueToTree(commit.getCommitDate().toString()));
            commitStat.put("linesDifference", objectMapper.valueToTree(commit.getLinesChanged()));
            commitStat.put("totalAdditions", objectMapper.valueToTree(commit.getLinesAdded()));
            commitStat.put("totalDeletions", objectMapper.valueToTree(commit.getLinesDeleted()));
            commitStat.put("files", objectMapper.valueToTree(fileStats));

            authorCommits.add(commitStat);
            churnStat.authorStats().put(author, authorCommits);
        }

        calculateChurn(churnStat);
    }

    @SneakyThrows
    private List<Map<String, JsonNode>> processCommitFiles(GHCommit commit, ChurnStat churnStat, String author, Long projectId) {
        List<Map<String, JsonNode>> fileStats = new ArrayList<>();
        List<GHCommit.File> files = commit.listFiles().toList();
        Filter filter = filterService.getFiltersByProjectId(projectId);

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
            fileStat.put("filename", objectMapper.valueToTree(file.getFileName()));
            fileStat.put("add all", objectMapper.valueToTree(file.getLinesAdded()));
            fileStat.put("del all", objectMapper.valueToTree(file.getLinesDeleted()));
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


    private void calculateChurn(ChurnStat churnStat) {
        for (Map.Entry<String, List<Map<String, JsonNode>>> entry : churnStat.authorStats().entrySet()) {
            String author = entry.getKey();
            Integer totalLines = churnStat.overall().get(author);
            var ref = new Object() {
                int addLines = 0;
            };
            churnStat.general().forEach((files, mapOfAdds) -> mapOfAdds.forEach((numberOfAdds, authorOfAdd) -> {
                if (authorOfAdd.equals(author)) {
                    ref.addLines = ref.addLines + 1;
                }
            }));

            for (String fileName : churnStat.general().keySet()) {
                Map<String, Integer> generalStats = new HashMap<>();
                churnStat.general().get(fileName).forEach((lineNumber, authorOfAdd) -> {
                    generalStats.merge(authorOfAdd, 1, Integer::sum);
                });
                churnStat.generalResult().put(fileName, generalStats);
            }
            if (totalLines != null && totalLines != 0) {
                churnStat.churn().put(author, 100 - (((double) ref.addLines / totalLines) * 100));
            } else {
                churnStat.churn().put(author, 0.0);
            }
        }
    }
}