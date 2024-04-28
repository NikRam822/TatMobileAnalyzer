package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Map;

public interface PatchScanService {
    ResponseEntity<Map<String,Object>> getStatisticPatchScan(String repositoryUrl, Date since, Date until, Long projectId);
}
