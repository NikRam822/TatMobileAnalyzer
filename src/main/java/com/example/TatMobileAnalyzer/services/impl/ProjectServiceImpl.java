package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.repository.ProjectRepository;
import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final FilterService filterService;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(FilterService filterService, ProjectRepository projectRepository) {
        this.filterService = filterService;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        Project newProject = new Project();
        newProject.setProjectName(project.getProjectName());
        newProject.setProjectLink(project.getProjectLink());

        Project savedProject = null;

        try {
            savedProject = projectRepository.save(newProject);
            filterService.createFilter(newProject);
            log.info("Saved project with link: {}", savedProject.getProjectLink());
        } catch (Exception e) {
            log.error("Error while saving project: {}", e.getMessage());
        }
        return savedProject;
    }

    @Override
    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            log.warn("Project with id {} not found", id);
        } else {
            log.info("Found project with id: {}", id);
        }
        return project;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        log.info("Found {} projects", projects.size());
        return projects;
    }

    @Override
    public Project findByProjectLink(String projectLink) {
        Project project = projectRepository.findByProjectLink(projectLink);
        if (project == null) {
            log.warn("Project with link {} not found", projectLink);
        } else {
            log.info("Found project with link: {}", projectLink);
        }
        return project;
    }

    @Override
    public Project updateProject(Project project) {
        Project updatedProject = projectRepository.save(project);
        log.info("Updated project with id: {}", updatedProject.getId());
        return updatedProject;
    }

    @Override
    public void deleteProject(Long id) {
        if (id != null) {
            projectRepository.deleteById(id);
            log.info("Deleted project with id: {}", id);
        } else {
            log.warn("Id of project to be deleted is null");
        }

    }
}
