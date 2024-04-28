package com.example.TatMobileAnalyzer.services;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.model.Project;

import java.util.List;

public interface FilterService {

    Filter createFilter(Project project);

    Filter getFilterById(Long id);

    List<Filter> getAllFilters();
    Filter getFiltersByProjectId(Long projectId);

    Filter updateFilter(Filter filter);

    void deleteFilter(Long id);


}
