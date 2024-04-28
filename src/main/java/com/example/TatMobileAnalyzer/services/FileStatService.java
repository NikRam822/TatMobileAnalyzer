package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

public interface FileStatService {
    ResponseEntity<String> getContributorsByFiles(String repoUrl);
}
