package com.example.TatMobileAnalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "filter")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ElementCollection
    @CollectionTable(name = "generated", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "value")
    private List<String> generated;

    @ElementCollection
    @CollectionTable(name = "test", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "value")
    private List<String> test;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=false)
    private Project project;
}

