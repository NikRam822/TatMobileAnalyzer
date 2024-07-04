package com.example.TatMobileAnalyzer.services;

import com.example.TatMobileAnalyzer.services.impl.churn.ChurnStat;
import com.example.TatMobileAnalyzer.services.impl.churn.SupportServices;

import java.util.Date;
import java.util.List;

public interface GitService {

    List<?> getCommitsPerPeriod(String repositoryUrl, Date since, Date until, String branch);

    boolean isValidRepository(String repositoryUrl);

    List<String> getBranches(String repositoryUrl);

    void processCommits(List<?> commitsPerPeriod, ChurnStat churnStat, Long projectId, SupportServices supportServices);
}
