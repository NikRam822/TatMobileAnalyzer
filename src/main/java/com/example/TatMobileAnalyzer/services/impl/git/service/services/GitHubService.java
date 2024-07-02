package com.example.TatMobileAnalyzer.services.impl.git.service.services;

import java.util.Date;
import java.util.List;

public interface GitHubService {

    List<?> getCommitsPerPeriod(String repositoryUrl, Date since, Date until);

    boolean isValidRepository(String repositoryUrl);
}
