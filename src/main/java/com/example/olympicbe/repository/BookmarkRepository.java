package com.example.olympicbe.repository;

import com.example.olympicbe.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
   Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);

   List<Bookmark> findByUserId(Long userId);
}
