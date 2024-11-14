package org.example.olympicbe.repository;

import org.example.olympicbe.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    @Query("SELECT s FROM Study s LEFT JOIN s.likesList l GROUP BY s.id ORDER BY COUNT(l) DESC, s.createdAt DESC")
    Page<Study> findTop3ByOrderByLikesCountDescAndCreatedAtDesc(Pageable pageable);

    // 특정 해시태그가 포함된 상위 3개의 공부법을 조회
    List<Study> findTop3ByHashtagsInAndIdNotOrderByCreatedAtDesc(List<String> hashtags, Long excludeId);

    // 혹은 특정 Subject가 같은 상위 3개의 공부법 조회
    @Query("SELECT s FROM Study s LEFT JOIN s.likesList l GROUP BY s.id ORDER BY COUNT(l) DESC")
    List<Study> findTop5ByOrderByLikesListDesc();  // 상위 5개만 정확히 제한

    // 특정 해시태그가 포함된 상위 3개의 공부법을 조회
    @Query("SELECT s FROM Study s JOIN s.hashtags h WHERE h IN :subjects ORDER BY s.createdAt DESC")
    List<Study> findTopByHashtagsInSubjects(@Param("subjects") List<String> subjects, Pageable pageable);
}


