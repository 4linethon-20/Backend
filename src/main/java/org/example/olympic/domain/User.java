package org.example.olympic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length= 20, nullable = false, unique= true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Column
    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "user_subjects", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "subject")
    private List<String> subjects = new ArrayList<>();

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private List<Study> studyList = new ArrayList<>();

}