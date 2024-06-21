package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.CocomoCalculateDto;
import com.example.TatMobileAnalyzer.dto.CocomoDto;
import com.example.TatMobileAnalyzer.services.CocomoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    ResponseEntity<?> calculate(@RequestBody CocomoDto cocomoDto) {

        CocomoCalculateDto cocomoCalculateDto = cocomoService.calculateCocomoMetrics(cocomoDto);
        return new ResponseEntity<>(cocomoCalculateDto, HttpStatus.OK);
    }
}
