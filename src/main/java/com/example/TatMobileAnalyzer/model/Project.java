package com.example.TatMobileAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_link")
    private String projectLink;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Collection<Statistic> statistic;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Collection<Filter> filters;

    @Column(name = "favorite")
    private Boolean favorite;

}