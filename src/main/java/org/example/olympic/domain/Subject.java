package org.example.olympic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subject {
    @Id
    private Long id;

    @Column
    private String subjectName;

    @OneToMany(mappedBy="subject", cascade= CascadeType.ALL)
    private List<Study> StudyList = new ArrayList<>();


}