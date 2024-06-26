package com.example.TatMobileAnalyzer.services.impl;

import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.repository.ProjectRepository;
import com.example.TatMobileAnalyzer.services.FavoriteProjectService;
import com.example.TatMobileAnalyzer.services.FilterService;
import com.example.TatMobileAnalyzer.services.GitHubService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService, FavoriteProjectService {

    private final FilterService filterService;
    private final ProjectRepository projectRepository;

    private final GitHubService gitHubService;

    @Autowired
    public ProjectServiceImpl(FilterService filterService, ProjectRepository projectRepository, GitHubService gitHubService) {
        this.filterService = filterService;
        this.projectRepository = projectRepository;
        this.gitHubService = gitHubService;
    }

    @Override
    @Transactional
    public Project createProject(Project project) {

        boolean isValidRepository = gitHubService.isValidRepository(project.getProjectLink());

        if (!isValidRepository) {
            log.error("Invalid repository with link: {}", project.getProjectLink());
            return null;
        }
        Project newProject = new Project();
        newProject.setProjectName(project.getProjectName());
        newProject.setProjectLink(project.getProjectLink());
        newProject.setFavorite(false);

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

    @Override
    public Project addFavoriteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null) {
            log.warn("Project with id {} not found", projectId);
        } else {
            project.setFavorite(true);
            projectRepository.save(project);
            log.info("Added project with id {} to favorites", projectId);
        }
        return project;
    }

    @Override
    public Project deleteFavoriteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project == null) {
            log.warn("Project with id {} not found", projectId);
        } else {
            project.setFavorite(false);
            projectRepository.save(project);
            log.info("Deleted project with id {} from favorites", projectId);
        }
        return project;
    }
}
