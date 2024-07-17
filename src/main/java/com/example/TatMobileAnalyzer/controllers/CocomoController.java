package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.CocomoCalculateDto;
import com.example.TatMobileAnalyzer.dto.CocomoDto;
import com.example.TatMobileAnalyzer.services.CocomoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("api/cocomo")
public class CocomoController {

    private final CocomoService cocomoService;

    @Autowired
    public CocomoController(CocomoService cocomoService) {
        this.cocomoService = cocomoService;
    }

    @PostMapping("/calculate")
    ResponseEntity<CocomoCalculateDto> calculate(@RequestBody CocomoDto cocomoDto) {

        try {
            CocomoCalculateDto cocomoCalculateDto = cocomoService.calculateCocomoMetrics(cocomoDto);
            return new ResponseEntity<>(cocomoCalculateDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Analyse error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
