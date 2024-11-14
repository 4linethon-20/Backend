package org.example.olympic.repository;

import org.example.olympic.domain.Bookmark;
import org.example.olympic.domain.User;
import org.example.olympic.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // userId를 User 엔티티를 통해 가져오도록 변경
    List<Bookmark> findByUser(User user);

    // userId와 postId를 각각 User와 Study 엔티티로 변경
    Optional<Bookmark> findByUserAndStudy(User user, Study study);
}
