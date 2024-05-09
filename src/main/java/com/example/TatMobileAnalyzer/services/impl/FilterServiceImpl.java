package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.repository.FilterRepository;
import com.example.TatMobileAnalyzer.services.FilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilterServiceImpl implements FilterService {
    private final FilterRepository filterRepository;

    @Autowired
    public FilterServiceImpl(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    @Override
    public Filter createFilter(Project project) {
        Filter filter = new Filter();
        filter.setProject(project);
        filter.setGenerated(new ArrayList<>());
        filter.setTest(new ArrayList<>());

        Filter savedFilter = null;
        try {
            savedFilter = filterRepository.save(filter);
            log.info("Saved filter with id: {}", savedFilter.getId());
        } catch (Exception e) {
            log.error("Error while saving filter: {}", e.getMessage());
        }
        return savedFilter;
    }

    @Override
    public Filter getFilterById(Long id) {
        Filter filter = filterRepository.findById(id).orElse(null);
        if (filter == null) {
            log.warn("Filter with id {} not found", id);
        }
        return filter;
    }

    @Override
    public List<Filter> getAllFilters() {
        List<Filter> filters = filterRepository.findAll();
        log.info("Found {} filters", filters.size());
        return filters;
    }


    public Filter getFiltersByProjectId(Long projectId) {
        Filter filter = filterRepository.findByProjectId(projectId);
        if (filter == null) {
            log.warn("Filter with id {} not found", projectId);
        }
        return filter;
    }

    @Override
    public Filter updateFilter(Filter filter) {
        Filter updatedFilter = filterRepository.save(filter);
        log.info("Updated filter with id: {}", updatedFilter.getId());
        return updatedFilter;
    }

    @Override
    public void deleteFilter(Long id) {
        if (id != null) {
            filterRepository.deleteById(id);
            log.info("Deleted filter with id: {}", id);
        } else {
            log.warn("Id of filter to delete is null");
        }

    }
}
