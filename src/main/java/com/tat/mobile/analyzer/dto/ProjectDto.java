package com.tat.mobile.analyzer.dto;

import com.tat.mobile.analyzer.model.Project;
import lombok.Data;

@Data
public class ProjectDto {
    private String projectName;
    private String projectLink;
    private Long projectId;
    private Boolean favorite;

    public static ProjectDto toProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectName(project.getProjectName());
        projectDto.setProjectLink(project.getProjectLink());
        projectDto.setProjectId(project.getId());
        projectDto.setFavorite(project.getFavorite());
        return projectDto;
    }
}
