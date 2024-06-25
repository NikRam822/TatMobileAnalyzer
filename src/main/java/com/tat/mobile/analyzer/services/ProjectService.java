package com.tat.mobile.analyzer.services;

import com.tat.mobile.analyzer.model.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    Project getProjectById(Long id);

    List<Project> getAllProjects();
    Project findByProjectLink(String projectLink);

    Project updateProject(Project project);

    void deleteProject(Long id);
}
