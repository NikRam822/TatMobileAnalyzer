package com.tat.mobile.analyzer.services;

import com.tat.mobile.analyzer.model.Filter;
import com.tat.mobile.analyzer.model.Project;

import java.util.List;

public interface FilterService {

    Filter createFilter(Project project);

    Filter getFilterById(Long id);

    List<Filter> getAllFilters();
    Filter getFiltersByProjectId(Long projectId);

    Filter updateFilter(Filter filter);

    void deleteFilter(Long id);


}
