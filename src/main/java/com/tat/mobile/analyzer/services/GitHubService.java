package com.tat.mobile.analyzer.services;

import org.kohsuke.github.GHCommit;

import java.util.Date;
import java.util.List;

public interface GitHubService {

    List<GHCommit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until);

    boolean isValidRepository(String repositoryUrl);
}
