package com.example.TatMobileAnalyzer.services;

import com.example.TatMobileAnalyzer.model.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    Project getProjectById(Long id);

    List<Project> getAllProjects();

    Project findByProjectLink(String projectLink);

    Project updateProject(Project project);

    void deleteProject(Long id);
}
