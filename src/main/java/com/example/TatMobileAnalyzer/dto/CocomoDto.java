package com.example.TatMobileAnalyzer.dto;

import com.example.TatMobileAnalyzer.services.impl.cocomo.ProjectType;
import lombok.Data;

@Data
public class CocomoDto {

    ProjectType projectType;
    double kloc;

    double reliability;
    double databaseSize;
    double productComplexity;
    double performance;
    double memoryLimit;
    double unstableEnvironment;
    double recoveryTime;

    double analyticalSkills;
    double developmentSkills;
    double developmentExperience;
    double virtualMachinesExperience;
    double languageExperience;

    double developmentTools;
    double developmentMethods;
    double developmentSchedule;

    double months;
    double personal;

}
