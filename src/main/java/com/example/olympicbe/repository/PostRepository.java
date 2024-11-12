package com.example.olympicbe.repository;

import com.example.olympicbe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
   Optional<Post> findById(Long postId);

   @Query("SELECT p FROM Post p WHERE " +
         "(COALESCE(:keyword, '') = '' OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
         "AND (COALESCE(:subjectName, '') = '' OR p.subject.name LIKE %:subjectName%)")
   Page<Post> searchByKeywordOrSubject(@Param("keyword") String keyword,
                                       @Param("subjectName") String subjectName,
                                       Pageable pageable);

}

