package com.example.TatMobileAnalyzer.services.impl.git.service;

import java.util.Date;
import java.util.List;

public interface IGitService {

    List<?> getCommitsPerPeriod(String repositoryUrl, Date since, Date until);

    boolean isValidRepository(String repositoryUrl);
}
