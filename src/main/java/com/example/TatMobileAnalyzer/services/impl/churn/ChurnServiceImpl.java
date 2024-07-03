package com.example.TatMobileAnalyzer.services.impl.churn;

import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.services.ChurnService;
import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import com.example.TatMobileAnalyzer.services.GitService;
import com.example.TatMobileAnalyzer.services.SingletonFactoryGitService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChurnServiceImpl implements ChurnService {

    FilterService filterService;
    ObjectMapper objectMapper;
    ProjectService projectService;

    @Autowired
    public ChurnServiceImpl(FilterService filterService,
                            ObjectMapper objectMapper,
                            ProjectService projectService) {
        this.filterService = filterService;
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

        Map<String, List<Map<String, JsonNode>>> authorStats = new HashMap<>();
        Map<String, Integer> overall = new HashMap<>();
        Map<String, Map<Integer, String>> general = new HashMap<>();
        Map<String, Map<String, Integer>> generalResult = new HashMap<>();
        Map<String, Double> churn = new HashMap<>();

        ChurnStat churnStat = new ChurnStat(authorStats, overall, general, generalResult, churn);


        GitService gitService = SingletonFactoryGitService.getInstance().getImplementation(project.getProjectLink());
        List<?> commitsPerPeriod = gitService.getCommitsPerPeriod(project.getProjectLink(), since, until);

        SupportServices supportServices = new SupportServices(filterService, objectMapper, projectService);
        gitService.processCommits(commitsPerPeriod, churnStat, projectId, supportServices);
        calculateChurn(churnStat);

        // Create a map to hold the JSON nodes
        Map<String, Object> finalStats = new HashMap<>();
        finalStats.put("authorStats", churnStat.authorStats());
        finalStats.put("overall", churnStat.overall());
        finalStats.put("general", churnStat.generalResult());
        finalStats.put("churn", churnStat.churn());

        // Return ResponseEntity with the map of JSON nodes
        return finalStats;
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