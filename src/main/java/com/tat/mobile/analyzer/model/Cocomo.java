package com.tat.mobile.analyzer.model;

import com.tat.mobile.analyzer.services.impl.cocomo.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cocomo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cocomo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    ProjectType projectType;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;
}
