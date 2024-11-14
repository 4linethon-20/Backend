package org.example.olympic.repository;

import org.example.olympic.domain.Bookmark;
import org.example.olympic.domain.Member;
import org.example.olympic.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // 특정 사용자의 북마크된 공부법 조회
    List<Bookmark> findByMember(Member member);

    // 특정 사용자가 특정 공부법을 북마크했는지 확인
    boolean existsByStudyAndMember(Study study, Member member);

    // 특정 사용자의 특정 공부법에 대한 북마크 삭제
    void deleteByStudyAndMember(Study study, Member member);
}
