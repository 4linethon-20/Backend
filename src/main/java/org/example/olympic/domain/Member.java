package org.example.olympic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name ="members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", length= 20, nullable = false, unique= true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Column
    private String profileImageUrl;

    @ElementCollection
    @CollectionTable(name = "member_subjects", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "subject")
    private List<String> subjects = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade=CascadeType.ALL)
    private List<Study> studyList = new ArrayList<>();
}
