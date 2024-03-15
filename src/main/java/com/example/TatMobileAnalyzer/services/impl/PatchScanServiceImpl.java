package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.PatchScanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommitQueryBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

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

        List<GHCommit> commitsPerPeriod = Lists.reverse(commitQueryBuilder.list().toList());

        Map<String, List<Map<String, Object>>> authorStats = new LinkedHashMap<>();
        Map<String, Integer> overall = new HashMap<>();
        Map<String, Map<Integer, String>> general = new HashMap<>();
        Map<String, Double> churn = new HashMap<>();

        for (GHCommit commit : commitsPerPeriod) {
            String author = commit.getAuthor().getLogin();
            List<Map<String, Object>> authorCommits = authorStats.getOrDefault(author, new ArrayList<>());

            List<GHCommit.File> files = commit.listFiles().toList();
            List<Map<String, Object>> fileStats = new ArrayList<>();

            for (GHCommit.File file : files) {
                String patch = file.getPatch();
                PatchReader.PatchInfo patchInfo = PatchReader.readPatch(patch);

                Map<String, Object> fileStat = new HashMap<>();
                fileStat.put("filename", file.getFileName());
                fileStat.put("add all", file.getLinesAdded());
                fileStat.put("del all", file.getLinesDeleted());
                fileStat.put("path", file.getPatch());

                fileStat.put("add", patchInfo.getAdd());
                fileStat.put("del", patchInfo.getDel());

                fileStats.add(fileStat);

                // Обновление статистики overall
                overall.put(author, overall.getOrDefault(author, 0) + patchInfo.getAdd().size());

                // Обновление статистики general
                Map<Integer, String> fileLines = general.getOrDefault(file.getFileName(), new HashMap<>());
                for (String addedLine : patchInfo.getAdd()) {
                    int lineNumber = Integer.parseInt(addedLine.split(". ")[0]);
                    fileLines.put(lineNumber, author);
                }
                general.put(file.getFileName(), fileLines);
            }

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

        // Расчет статистики churn
        for (Map.Entry<String, List<Map<String, Object>>> entry : authorStats.entrySet()) {
            String author = entry.getKey();
            Integer totalLines = overall.get(author);
            int addedLines = entry.getValue().size();
            if (totalLines != null && totalLines != 0) {
                churn.put(author, ((double) addedLines / totalLines));
            } else {
                // Обработка ситуации, когда totalLines равно null или 0
                // Например, можно присвоить churn значение 0 или добавить в лог сообщение об ошибке
                churn.put(author, 0.0);
            }
        }


        // Добавление новой статистики в итоговый JSON
        Map<String, Object> finalStats = new HashMap<>();
        finalStats.put("authorStats", authorStats);
        finalStats.put("overall", overall);
        finalStats.put("general", general);
        finalStats.put("churn", churn);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(finalStats);

        return ResponseEntity.ok(json);
    }
}