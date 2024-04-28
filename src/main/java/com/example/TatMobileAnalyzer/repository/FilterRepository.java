package com.example.TatMobileAnalyzer.repository;

import com.example.TatMobileAnalyzer.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    Filter findByProjectId(Long projectId);
}
