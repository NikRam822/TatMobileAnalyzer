package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;

public interface ChurnService {
    ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, Date since, Date until, Long projectId);
}
