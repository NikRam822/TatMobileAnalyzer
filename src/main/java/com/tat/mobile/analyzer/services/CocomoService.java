package com.tat.mobile.analyzer.services;

import com.tat.mobile.analyzer.dto.CocomoCalculateDto;
import com.tat.mobile.analyzer.dto.CocomoDto;

public interface CocomoService {

    CocomoCalculateDto calculateCocomoMetrics(CocomoDto cocomoDto);
}
