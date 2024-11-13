package org.example.olympic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.olympic.domain.common.BaseEntity;


@Getter
@Setter
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int dislikeCount = 0;
}
