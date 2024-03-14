package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.RepositoryDto;
import com.example.TatMobileAnalyzer.services.LocFilesService;
import com.example.TatMobileAnalyzer.services.PatchScanService;
import com.example.TatMobileAnalyzer.services.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("")
public class StatisticController {

    private final StatisticService statisticService;
    private final LocFilesService locFilesService;
    private final PatchScanService patchScanService;


    @Autowired
    public StatisticController(StatisticService statisticService, LocFilesService locFilesService, PatchScanService patchScanService) {
        this.statisticService = statisticService;
        this.locFilesService = locFilesService;;
        this.patchScanService = patchScanService;
    }

    @GetMapping("/")
    ResponseEntity<String> getAll() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping("/repository")
    ResponseEntity<String> getCommitStatistic(@RequestBody RepositoryDto repositoryDto) {
        return statisticService.getStatistic(repositoryDto.getRepositoryUrl());
    }

    @PostMapping("/loc/statistic")
    ResponseEntity<String> getStatisticLocFiles(@RequestParam(required = false) String since,
                                                @RequestParam(required = false) String until,
                                                @RequestBody RepositoryDto repositoryDto) {
        String period;
        if (since != null || until != null) {
            if (since == null) {
                period = "?until=" + until;
            } else if (until == null) {
                period = "?since=" + since;
            } else {
                period = "?since=" + since + "&until=" + until;
            }
        } else {
            period = "";
        }
        return locFilesService.getStatisticLocFiles(repositoryDto.getRepositoryUrl(), period);
    }
    @PostMapping("/patch/statistic")
    ResponseEntity<String> getStatisticPatchScan(@RequestParam(required = false) String since,
                                                @RequestParam(required = false) String until,
                                                @RequestBody RepositoryDto repositoryDto) {
        String period;
        if (since != null || until != null) {
            if (since == null) {
                period = "?until=" + until;
            } else if (until == null) {
                period = "?since=" + since;
            } else {
                period = "?since=" + since + "&until=" + until;
            }
        } else {
            period = "";
        }
        return patchScanService.getStatisticPatchScan(repositoryDto.getRepositoryUrl(), period);
    }
}