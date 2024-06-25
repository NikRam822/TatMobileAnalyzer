package com.tat.mobile.analyzer.services;

import com.tat.mobile.analyzer.model.Project;

public interface FavoriteProjectService {

    Project addFavoriteProject(Long projectId);

    Project deleteFavoriteProject(Long projectId);
}
