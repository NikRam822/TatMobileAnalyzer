package com.tat.mobile.analyzer.repository;

import com.tat.mobile.analyzer.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    Filter findByProjectId(Long projectId);
}
