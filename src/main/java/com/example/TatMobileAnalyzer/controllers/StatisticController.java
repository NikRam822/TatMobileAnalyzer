package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.ProjectAnalysisDto;
import com.example.TatMobileAnalyzer.services.ChurnService;
import com.example.TatMobileAnalyzer.services.FileStatService;
import com.example.TatMobileAnalyzer.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    private final ChurnService churnService;

    private final FileStatService fileStatService;


    @Autowired
    public StatisticController(FileStatService fileStatService, ChurnService churnService) {
        this.fileStatService = fileStatService;
        this.churnService = churnService;
    }

    @PostMapping("/churn")
    ResponseEntity<Map<String, Object>> getStatisticChurn(@RequestParam(required = false) String since,
                                                          @RequestParam(required = false) String until,
                                                          @RequestBody ProjectAnalysisDto projectAnalyseDto) throws ParseException {
        since = since != null ? since + " 00:00:00" : since;
        until = until != null ? until + " 23:59:59" : until;
        Date startDate = DateUtils.parseDate(since, "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtils.parseDate(until, "yyyy-MM-dd HH:mm:ss");

        Map<String, Object> statisticChurn = churnService.getStatisticChurn(startDate, endDate, projectAnalyseDto.getProjectId(), projectAnalyseDto.getBranch());

        if (statisticChurn == null) {
            log.error("Error with analyze project with id: {}", projectAnalyseDto.getProjectId());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(statisticChurn, HttpStatus.OK);
    }

    @PostMapping("/files")
    ResponseEntity<String> getFileStatistic(@RequestParam(required = false) String since,
                                            @RequestParam(required = false) String until,
                                            @RequestBody String repositoryLink,
                                            @RequestBody String branch) throws ParseException {

        Date startDate = DateUtils.parseDate(since + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtils.parseDate(until + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

        Map<String, List<Map<String, Object>>> fileStatistic = fileStatService.getContributorsByFiles(repositoryLink, startDate, endDate, branch);
        return new ResponseEntity<>(fileStatistic.toString(), HttpStatus.OK);
    }

}