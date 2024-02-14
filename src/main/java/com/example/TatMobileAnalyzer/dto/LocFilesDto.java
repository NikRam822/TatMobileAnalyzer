package com.example.TatMobileAnalyzer.dto;

import lombok.Data;

@Data
public class LocFilesDto {
    private String repositoryUrl;

    private String since;

    private String until;
}
