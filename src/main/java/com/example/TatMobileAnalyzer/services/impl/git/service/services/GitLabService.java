package com.example.TatMobileAnalyzer.services.impl.git.service.services;

import org.gitlab4j.api.models.Commit;

import java.util.Date;
import java.util.List;

public interface GitLabService {

    List<Commit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until);

    boolean isValidRepository(String repositoryUrl);
}
