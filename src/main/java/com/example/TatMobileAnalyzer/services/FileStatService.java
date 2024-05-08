package com.example.TatMobileAnalyzer.services;

import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FileStatService {
    Map<String, List<Map<String, Object>>> getContributorsByFiles(String repoUrl, Date since, Date until);
}
