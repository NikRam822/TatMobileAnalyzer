package com.tat.mobile.analyzer.controllers;

import com.tat.mobile.analyzer.dto.CocomoCalculateDto;
import com.tat.mobile.analyzer.dto.CocomoDto;
import com.tat.mobile.analyzer.services.CocomoService;
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
    ResponseEntity<CocomoCalculateDto> calculate(@RequestBody CocomoDto cocomoDto) {

        CocomoCalculateDto cocomoCalculateDto = cocomoService.calculateCocomoMetrics(cocomoDto);
        return new ResponseEntity<>(cocomoCalculateDto, HttpStatus.OK);
    }
}
