package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.RepositoryDto;
import com.example.TatMobileAnalyzer.services.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("")
public class HelloWorld {

    private final Logger LOGGER = log;
    private final StatisticService statisticService;

    @Autowired
    public HelloWorld(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/")
    ResponseEntity<String> getAll() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping("/repository")
    ResponseEntity getStatistic(@RequestBody RepositoryDto repositoryDto) {
       return statisticService.getStatistic(repositoryDto.getRepositoryUrl());



    }
}
