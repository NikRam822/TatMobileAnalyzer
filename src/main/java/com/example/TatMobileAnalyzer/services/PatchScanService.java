package com.example.TatMobileAnalyzer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

public interface PatchScanService {
    ResponseEntity<String> getStatisticPatchScan(String repositoryUrl, Date since, Date until);
}
