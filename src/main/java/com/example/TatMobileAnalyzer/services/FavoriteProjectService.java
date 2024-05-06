package com.example.TatMobileAnalyzer.services;

import com.example.TatMobileAnalyzer.model.Project;

public interface FavoriteProjectService {

    Project addFavoriteProject(Long projectId);

    Project deleteFavoriteProject(Long projectId);
}
