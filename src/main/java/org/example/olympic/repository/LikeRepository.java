package org.example.olympic.repository;

import org.example.olympic.domain.Likes;
import org.example.olympic.domain.User;
import org.example.olympic.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    // 특정 공부법에 대한 좋아요 개수 조회
    long countByStudy(Study study);

    // 특정 유저가 특정 공부법에 좋아요를 눌렀는지 확인
    boolean existsByStudyAndUser(Study study, User user);

    // 특정 유저가 특정 공부법에 대한 좋아요 삭제
    void deleteByStudyAndUser(Study study, User user);
}
