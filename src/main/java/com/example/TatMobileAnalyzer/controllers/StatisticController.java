package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.ProjectDto;
import com.example.TatMobileAnalyzer.dto.RepositoryDto;
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

@CrossOrigin
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
                                                          @RequestBody ProjectDto projectDto) throws ParseException {
        Date startDate = DateUtils.parseDate(since, "yyyy-MM-dd");
        Date endDate = DateUtils.parseDate(until, "yyyy-MM-dd");

        Map<String, Object> statisticChurn = churnService.getStatisticChurn(projectDto.getProjectLink(), startDate, endDate, projectDto.getProjectId());
        return new ResponseEntity<>(statisticChurn, HttpStatus.OK);
    }

    @PostMapping("/files")
    ResponseEntity<String> getFileStatistic(@RequestParam(required = false) String since,
                                            @RequestParam(required = false) String until,
                                            @RequestBody RepositoryDto repositoryDto) throws ParseException {

        Date startDate = DateUtils.parseDate(since, "yyyy-MM-dd");
        Date endDate = DateUtils.parseDate(until, "yyyy-MM-dd");

        Map<String, List<Map<String, Object>>> fileStatistic = fileStatService.getContributorsByFiles(repositoryDto.getRepositoryUrl(), startDate, endDate);
        return new ResponseEntity<>(fileStatistic.toString(), HttpStatus.OK);
    }

}