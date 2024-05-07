package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface FileStatService {
    ResponseEntity<String> getContributorsByFiles(String repoUrl, Date since, Date until);
}
