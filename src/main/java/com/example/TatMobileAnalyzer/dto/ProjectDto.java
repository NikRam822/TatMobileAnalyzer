package com.example.TatMobileAnalyzer.dto;

import com.example.TatMobileAnalyzer.model.Project;
import lombok.Data;

@Data
public class ProjectDto {
    private String projectName;
    private String projectLink;
    private Long projectId;

    public static ProjectDto toProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectName(project.getProjectName());
        projectDto.setProjectLink(project.getProjectLink());
        projectDto.setProjectId(project.getId());
        return projectDto;
    }
}
