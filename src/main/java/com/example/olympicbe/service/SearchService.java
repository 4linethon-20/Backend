package com.example.olympicbe.service;

import com.example.olympicbe.entity.Post;
import com.example.olympicbe.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
   private final PostRepository postRepository;
   @Autowired
   public SearchService(PostRepository postRepository) {
      this.postRepository = postRepository;
   }

   public Page<Post> searchPosts(String searchInput, Pageable pageable) {
      if (searchInput.startsWith("#")) {
         String subjectName = searchInput.substring(1);
         return postRepository.searchByKeywordOrSubject("", subjectName, pageable);
      } else {
         return postRepository.searchByKeywordOrSubject(searchInput, "", pageable);
      }
   }



}
