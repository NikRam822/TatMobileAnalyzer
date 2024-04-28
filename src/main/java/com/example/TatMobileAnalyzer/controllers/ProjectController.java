package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.ProjectDto;
import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping("/create")
    ResponseEntity createProject(@RequestBody ProjectDto projectDto) {

        Project project = projectService.findByProjectLink(projectDto.getProjectLink());
        if (project != null) {
            log.warn("Project with link {} already exists", projectDto.getProjectLink());
            return new ResponseEntity<>("Project with this link is already exist", HttpStatus.CONFLICT);
        }
        project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectLink(projectDto.getProjectLink());
        Project createdProject = projectService.createProject(project);

        return new ResponseEntity<>(ProjectDto.toProjectDto(createdProject), HttpStatus.CREATED);
    }

    @GetMapping("/get-projects")
    ResponseEntity<List<ProjectDto>> getProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDto> projectsDto = projects.stream().map(ProjectDto::toProjectDto).toList();
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }
}
