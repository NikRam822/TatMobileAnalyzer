package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

public interface LocFilesService {
    ResponseEntity<String> getStatisticLocFiles(String repositoryUrl, String period);
}
