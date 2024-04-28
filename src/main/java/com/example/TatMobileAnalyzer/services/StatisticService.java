package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

public interface StatisticService {
    ResponseEntity<String> getStatistic(String repoUrl);
}
