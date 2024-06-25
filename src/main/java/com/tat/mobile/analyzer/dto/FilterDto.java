package com.tat.mobile.analyzer.dto;

import com.tat.mobile.analyzer.model.Filter;
import lombok.Data;

import java.util.List;

@Data
public class FilterDto {

    private List<String> Generated;
    private List<String> Test;
    private Long projectId;

    public static FilterDto toFilterDto(Filter filter) {
        FilterDto filterDto = new FilterDto();
        filterDto.setGenerated(filter.getGenerated());
        filterDto.setTest(filter.getTest());
        return filterDto;
    }
}
