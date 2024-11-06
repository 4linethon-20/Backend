package com.example.olympicbe.repository;

import com.example.olympicbe.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
   Optional<Post> findById(Long postId);
}
