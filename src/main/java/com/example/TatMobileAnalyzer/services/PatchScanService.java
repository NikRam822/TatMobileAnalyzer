package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface PatchScanService {
    ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, Date since, Date until);
}
