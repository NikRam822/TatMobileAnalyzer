package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.services.PatchScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PatchScanServiceImpl implements PatchScanService {
    @Override
    public ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, String period) {
        return null;
    }
}
