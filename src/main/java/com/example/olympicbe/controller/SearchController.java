package com.example.olympicbe.controller;

import com.example.olympicbe.entity.Post;
import com.example.olympicbe.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
   private final SearchService searchService;
   private final PagedResourcesAssembler<Post> pagedResourcesAssembler;


   @Autowired
   public SearchController(SearchService searchService, PagedResourcesAssembler<Post> pagedResourcesAssembler) {
      this.searchService = searchService;
      this.pagedResourcesAssembler = pagedResourcesAssembler;
   }

   @GetMapping
   public PagedModel<EntityModel<Post>> search(
         @RequestParam("searchInput") String searchInput,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      Pageable pageable = PageRequest.of(page, size);
      System.out.println("searchInput: " + searchInput);  // 로그로 searchInput 확인
      Page<Post> postsPage = searchService.searchPosts(searchInput, pageable);
      return pagedResourcesAssembler.toModel(postsPage);
   }
}
