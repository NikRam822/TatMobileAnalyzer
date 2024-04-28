package com.example.TatMobileAnalyzer.repository;

import com.example.TatMobileAnalyzer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectLink(String projectLink);
}
