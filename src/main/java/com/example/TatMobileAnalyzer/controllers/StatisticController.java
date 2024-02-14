package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.LocFilesDto;
import com.example.TatMobileAnalyzer.dto.RepositoryDto;
import com.example.TatMobileAnalyzer.services.StatisticService;
import com.example.TatMobileAnalyzer.services.LocFilesService;
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


    @Autowired
    public StatisticController(StatisticService statisticService, LocFilesService locFilesService) {
        this.statisticService = statisticService;
        this.locFilesService = locFilesService;
    }

    @GetMapping("/")
    ResponseEntity<String> getAll() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping("/repository")
    ResponseEntity<String> getStatistic(@RequestBody RepositoryDto repositoryDto) {
        return statisticService.getStatistic(repositoryDto.getRepositoryUrl());
    }

    @PostMapping("/loc/statistic")
    ResponseEntity<String> getStatisticLocFiles(@RequestBody LocFilesDto locFilesDto) {
        return locFilesService.getStatisticLocFiles(locFilesDto.getRepositoryUrl(), locFilesDto.getSince(), locFilesDto.getUntil());
    }
}