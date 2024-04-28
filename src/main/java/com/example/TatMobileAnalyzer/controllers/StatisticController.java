package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.ProjectDto;
import com.example.TatMobileAnalyzer.dto.RepositoryDto;
import com.example.TatMobileAnalyzer.services.FileStatService;
import com.example.TatMobileAnalyzer.services.LocFilesService;
import com.example.TatMobileAnalyzer.services.PatchScanService;
import com.example.TatMobileAnalyzer.services.StatisticService;
import com.example.TatMobileAnalyzer.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("")
public class StatisticController {

    private final StatisticService statisticService;
    private final LocFilesService locFilesService;
    private final PatchScanService patchScanService;

    private final FileStatService fileStatService;


    @Autowired
    public StatisticController(StatisticService statisticService,
                               LocFilesService locFilesService,
                               FileStatService fileStatService,
                               PatchScanService patchScanService) {
        this.statisticService = statisticService;
        this.locFilesService = locFilesService;
        this.fileStatService = fileStatService;
        this.patchScanService = patchScanService;
    }

    @GetMapping("/api/")
    ResponseEntity<String> getAll() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping("/api/repository")
    ResponseEntity<String> getCommitStatistic(@RequestBody RepositoryDto repositoryDto) {
        return statisticService.getStatistic(repositoryDto.getRepositoryUrl());
    }

    @PostMapping("/api/loc/statistic")
    ResponseEntity<String> getStatisticLocFiles(@RequestParam(required = false) String since,
                                                @RequestParam(required = false) String until,
                                                @RequestBody RepositoryDto repositoryDto) {
        String period = "";
        if (since != null || until != null) {
            period = (since == null) ? "?until=" + until :
                    (until == null) ? "?since=" + since :
                            "?since=" + since + "&until=" + until;
        }
        return locFilesService.getStatisticLocFiles(repositoryDto.getRepositoryUrl(), period);
    }

    @PostMapping("api/patch/statistic")
    ResponseEntity<Map<String, Object>> getStatisticPatchScan(@RequestParam(required = false) String since,
                                                              @RequestParam(required = false) String until,
                                                              @RequestBody ProjectDto projectDto) throws ParseException {
        Date startDate = DateUtils.parseDate(since, "yyyy-MM-dd");
        Date endDate = DateUtils.parseDate(until, "yyyy-MM-dd");

        return patchScanService.getStatisticPatchScan(projectDto.getProjectLink(), startDate, endDate, projectDto.getProjectId());
    }

    @PostMapping("api/files/statistics")
    ResponseEntity<String> getFileStatistic(@RequestBody RepositoryDto repositoryDto) {
        return fileStatService.getContributorsByFiles(repositoryDto.getRepositoryUrl());
    }

}