package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.FilterDto;
import com.example.TatMobileAnalyzer.dto.ProjectIdDto;
import com.example.TatMobileAnalyzer.model.Filter;
import com.example.TatMobileAnalyzer.services.FilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("api/filter")
public class FilterController {

    private final FilterService filterService;

    @Autowired
    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @PostMapping("/get-filters-for-project")
    ResponseEntity<FilterDto> getAll(@RequestBody ProjectIdDto projectIdDto) {
        Filter filter = filterService.getFiltersByProjectId(projectIdDto.getProjectId());
        if (filter == null) {
            log.error("No filter found for project {}", projectIdDto.getProjectId());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(FilterDto.toFilterDto(filter), HttpStatus.OK);
    }

    @PutMapping("/update-filter")
    ResponseEntity<FilterDto> updateFilter(@RequestBody FilterDto filterDto) {

        Filter filter = filterService.getFiltersByProjectId(filterDto.getProjectId());
        if (filter == null) {
            log.error("No filters found for project {}", filterDto.getProjectId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        filter.setTest(filterDto.getTest());
        filter.setGenerated(filterDto.getGenerated());

        Filter updatedFilter = filterService.updateFilter(filter);

        return new ResponseEntity<>(FilterDto.toFilterDto(updatedFilter), HttpStatus.OK);
    }
}
