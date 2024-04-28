package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.FilterDto;
import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.services.FilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/filter")
public class FilterController {

    private final FilterService filterService;

    @Autowired
    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("/get-filters-for-project")
    ResponseEntity<FilterDto> getAll(@RequestParam Long projectId) {
        Filter filter = filterService.getFiltersByProjectId(projectId);
        if (filter == null) {
            log.error("No filters found for project " + projectId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(FilterDto.toFilterDto(filter), HttpStatus.OK);
    }

    @PutMapping("/update-filter")
    ResponseEntity<FilterDto> createProject(@RequestBody FilterDto filterDto, @RequestParam Long projectId) {

        Filter filter = filterService.getFiltersByProjectId(projectId);
        if (filter == null) {
            log.error("No filters found for project " + projectId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        filter.setTest(filterDto.getTest());
        filter.setGenerated(filterDto.getGenerated());

        Filter updatedFilter = filterService.updateFilter(filter);

        return new ResponseEntity<>(FilterDto.toFilterDto(updatedFilter), HttpStatus.OK);
    }
}
