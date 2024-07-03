package com.example.TatMobileAnalyzer.services.impl.churn;

import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

public record SupportServices(FilterService filterService, ObjectMapper objectMapper, ProjectService projectService) {
}
