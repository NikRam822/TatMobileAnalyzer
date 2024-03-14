package com.example.TatMobileAnalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public interface PatchScanService {
    ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, String period);
}
