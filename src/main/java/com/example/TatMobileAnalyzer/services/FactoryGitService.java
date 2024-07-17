package com.example.TatMobileAnalyzer.services;

public interface FactoryGitService {

    GitService getImplementation(String repositoryUrl);
}
