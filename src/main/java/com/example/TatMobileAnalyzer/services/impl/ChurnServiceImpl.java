package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.services.ChurnService;
import lombok.SneakyThrows;
import org.kohsuke.github.GHCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChurnServiceImpl implements ChurnService {

    FilterService filterService;
    GitHubService gitHubService;

    @Autowired
    public ChurnServiceImpl(GitHubService gitHubService, FilterService filterService) {
        this.filterService = filterService;
        this.gitHubService = gitHubService;
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Map<String, Object>> getStatisticPatchScan(String repositoryUrl, Date since, Date until, Long projectId) {

        List<GHCommit> commitsPerPeriod = gitHubService.getCommitsPerPeriod(repositoryUrl, since, until);

        Map<String, List<Map<String, Object>>> authorStats = new LinkedHashMap<>();
        Map<String, Integer> overall = new HashMap<>();
        Map<String, Map<Integer, String>> general = new HashMap<>();
        Map<String, Map<String, Integer>> generalResult = new HashMap<>();
        Map<String, Double> churn = new HashMap<>();

        processCommits(commitsPerPeriod, authorStats, overall, general, generalResult, churn, projectId);

        Map<String, Object> finalStats = new HashMap<>();
        finalStats.put("authorStats", authorStats);
        finalStats.put("overall", overall);
        finalStats.put("general", generalResult);
        finalStats.put("churn", churn);

        return ResponseEntity.ok(finalStats);
    }

    @SneakyThrows
    private void processCommits(List<GHCommit> commitsPerPeriod, Map<String, List<Map<String, Object>>> authorStats,
                                Map<String, Integer> overall, Map<String, Map<Integer, String>> general,
                                Map<String, Map<String, Integer>> generalResult, Map<String, Double> churn, Long projectId) {
        for (GHCommit commit : commitsPerPeriod) {
            String author = commit.getCommitShortInfo().getAuthor().getName();
            List<Map<String, Object>> authorCommits = authorStats.getOrDefault(author, new ArrayList<>());

            List<Map<String, Object>> fileStats = processCommitFiles(commit, general, overall, author, projectId);

            Map<String, Object> commitStat = new HashMap<>();
            commitStat.put("sha", commit.getSHA1());
            commitStat.put("date", commit.getCommitDate().toString());
            commitStat.put("linesDifference", commit.getLinesChanged());
            commitStat.put("totalAdditions", commit.getLinesAdded());
            commitStat.put("totalDeletions", commit.getLinesDeleted());
            commitStat.put("files", fileStats);

            authorCommits.add(commitStat);
            authorStats.put(author, authorCommits);
        }

        calculateChurn(authorStats, overall, general, generalResult, churn);
    }

    @SneakyThrows
    private List<Map<String, Object>> processCommitFiles(GHCommit commit, Map<String, Map<Integer, String>> general,
                                                         Map<String, Integer> overall, String author, Long projectId) {
        List<Map<String, Object>> fileStats = new ArrayList<>();

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

            Map<String, Object> fileStat = new HashMap<>();
            fileStat.put("filename", file.getFileName());
            fileStat.put("add all", file.getLinesAdded());
            fileStat.put("del all", file.getLinesDeleted());
            //fileStat.put("path", file.getPatch());

            // fileStat.put("add", patchInfo.getAdd());
            // fileStat.put("del", patchInfo.getDel());

            fileStats.add(fileStat);
            overall.put(author, overall.getOrDefault(author, 0) + patchInfo.getAdd().size());

            updateGeneralStatistics(file, patchInfo, general, author);
        }
        return fileStats;
    }

    @SneakyThrows
    private void updateGeneralStatistics(GHCommit.File file, PatchReader.PatchInfo patchInfo, Map<String, Map<Integer, String>> general, String author) {
        Map<Integer, String> fileLines = general.getOrDefault(file.getFileName(), new HashMap<>());
        for (String addedLine : patchInfo.getAdd()) {
            int lineNumber = Integer.parseInt(addedLine.split(". ")[0]);
            fileLines.put(lineNumber, author);
        }
        general.put(file.getFileName(), fileLines);
    }


    private void calculateChurn(Map<String, List<Map<String, Object>>> authorStats, Map<String, Integer> overall,
                                Map<String, Map<Integer, String>> general, Map<String, Map<String, Integer>> generalResult,
                                Map<String, Double> churn) {
        for (Map.Entry<String, List<Map<String, Object>>> entry : authorStats.entrySet()) {
            String author = entry.getKey();
            Integer totalLines = overall.get(author);
            var ref = new Object() {
                int addLines = 0;
            };
            general.forEach((files, mapOfAdds) -> mapOfAdds.forEach((numberOfAdds, authorOfAdd) -> {
                if (authorOfAdd.equals(author)) {
                    ref.addLines = ref.addLines + 1;
                }
            }));

            for (String fileName : general.keySet()) {
                Map<String, Integer> generalStats = new HashMap<>();
                general.get(fileName).forEach((lineNumber, authorOfAdd) -> {
                    generalStats.merge(authorOfAdd, 1, Integer::sum);
                });
                generalResult.put(fileName, generalStats);
            }
            if (totalLines != null && totalLines != 0) {
                churn.put(author, 100 - (((double) ref.addLines / totalLines) * 100));
            } else {
                churn.put(author, 0.0);
            }
        }
    }
}
