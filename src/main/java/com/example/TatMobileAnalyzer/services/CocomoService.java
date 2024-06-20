package com.example.TatMobileAnalyzer.services;

import com.example.TatMobileAnalyzer.dto.CocomoCalculateDto;
import com.example.TatMobileAnalyzer.dto.CocomoDto;

public interface CocomoService {

    CocomoCalculateDto calculateCocomoMetrics(CocomoDto cocomoDto);
}
