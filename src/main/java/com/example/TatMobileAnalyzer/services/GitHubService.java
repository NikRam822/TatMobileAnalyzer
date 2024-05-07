package com.example.TatMobileAnalyzer.services;

import org.kohsuke.github.GHCommit;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GitHubService {

    List<GHCommit> getCommitsPerPeriod(String repositoryUrl, Date since, Date until);
}
