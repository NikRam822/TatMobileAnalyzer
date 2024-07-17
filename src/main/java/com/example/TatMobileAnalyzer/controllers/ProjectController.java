package com.example.TatMobileAnalyzer.controllers;

import com.example.TatMobileAnalyzer.dto.ProjectDto;
import com.example.TatMobileAnalyzer.dto.ProjectIdDto;
import com.example.TatMobileAnalyzer.model.Project;
import com.example.TatMobileAnalyzer.services.FactoryGitService;
import com.example.TatMobileAnalyzer.services.FavoriteProjectService;
import com.example.TatMobileAnalyzer.services.GitService;
import com.example.TatMobileAnalyzer.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final FavoriteProjectService favoriteProjectService;
    private final FactoryGitService factoryGitService;

    @Autowired
    public ProjectController(ProjectService projectService, FavoriteProjectService favoriteProjectService, FactoryGitService factoryGitService) {
        this.projectService = projectService;
        this.favoriteProjectService = favoriteProjectService;
        this.factoryGitService = factoryGitService;
    }


    @PostMapping("/create")
    ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {

        Project project = projectService.findByProjectLink(projectDto.getProjectLink());
        if (project != null) {
            log.warn("Project with link {} already exists", projectDto.getProjectLink());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setProjectLink(projectDto.getProjectLink());
        Project createdProject = projectService.createProject(project);

        if (createdProject == null) {
            log.warn("Project with link {} was not created", projectDto.getProjectLink());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ProjectDto.toProjectDto(createdProject), HttpStatus.CREATED);
    }

    @GetMapping("/get-projects")
    ResponseEntity<List<ProjectDto>> getProjects() {
        List<Project> projects = projectService.getAllProjects();

        if (projects.isEmpty()) {
            log.warn("No projects found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ProjectDto> projectsDto = projects.stream().map(ProjectDto::toProjectDto).toList();
        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }

    @PostMapping("/get-branches")
    ResponseEntity<List<String>> getBranches(@RequestBody ProjectIdDto projectIdDto) {
        Project project = projectService.getProjectById(projectIdDto.getProjectId());
        String projectLink = project.getProjectLink();

        GitService gitService = factoryGitService.getImplementation(project.getProjectLink());
        List<String> branches = gitService.getBranches(projectLink);
        return new ResponseEntity<>(branches, HttpStatus.OK);
    }

    @DeleteMapping("/delete-project")
    ResponseEntity<String> deleteProject(@RequestBody ProjectIdDto projectIdDto) {
        try {
            projectService.deleteProject(projectIdDto.getProjectId());
            return new ResponseEntity<>("Project is deleted", HttpStatus.OK);
        } catch (Exception e) {
            log.error("The project with id: {} has not been deleted. Error: {}", projectIdDto.getProjectId(), e.getMessage());
            return new ResponseEntity<>("The project has not been deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/favorite/add")
    ResponseEntity<String> addProjectToFavorites(@RequestBody ProjectIdDto projectIdDto) {

        Project project = favoriteProjectService.addFavoriteProject(projectIdDto.getProjectId());
        if (project == null) {
            log.warn("Project with id {} does not exist", projectIdDto.getProjectId());
            return new ResponseEntity<>("Project with id: " + projectIdDto.getProjectId() + " does not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Project with id: " + projectIdDto.getProjectId() + " is added to favorites", HttpStatus.OK);
        }
    }

    @PostMapping("/favorite/delete")
    ResponseEntity<String> deleteProjectFromFavorites(@RequestBody ProjectIdDto projectIdDto) {

        Project project = favoriteProjectService.deleteFavoriteProject(projectIdDto.getProjectId());
        if (project == null) {
            log.warn("Project with id {} does not exist", projectIdDto.getProjectId());
            return new ResponseEntity<>("Project with id: " + projectIdDto.getProjectId() + " does not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Project with id: " + projectIdDto.getProjectId() + " is deleted from favorites", HttpStatus.OK);
        }
    }

}
