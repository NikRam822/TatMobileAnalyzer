package com.tat.mobile.analyzer.repository;

import com.tat.mobile.analyzer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectLink(String projectLink);
}
