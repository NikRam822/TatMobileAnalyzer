package com.example.TatMobileAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sha_commit")
    private String shaCommit;

    @Column(name = "patch")
    private String patch;

    @Column(name = "add_numbers")
    private int addNumbers;

    @Column(name = "commit_numbers")
    private int commitNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

}