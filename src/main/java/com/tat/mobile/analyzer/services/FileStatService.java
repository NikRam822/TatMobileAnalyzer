package com.tat.mobile.analyzer.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FileStatService {
    Map<String, List<Map<String, Object>>> getContributorsByFiles(String repoUrl, Date since, Date until);
}
