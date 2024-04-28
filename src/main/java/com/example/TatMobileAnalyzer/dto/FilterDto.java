package com.example.TatMobileAnalyzer.dto;

import com.example.TatMobileAnalyzer.model.Filter;
import lombok.Data;

import java.util.List;

@Data
public class FilterDto {

    private List<String> Generated;
    private List<String> Test;

    public static FilterDto toFilterDto(Filter filter) {
        FilterDto filterDto = new FilterDto();
        filterDto.setGenerated(filter.getGenerated());
        filterDto.setTest(filter.getTest());
        return filterDto;
    }
}
