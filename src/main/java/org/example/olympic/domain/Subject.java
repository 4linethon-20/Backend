package org.example.olympic.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    @Id
    private Long id;

    @Column
    private String subjectName;

    @OneToMany(mappedBy="study", cascade= CascadeType.ALL)
    private List<Study> StudyList = new ArrayList<>();
}
