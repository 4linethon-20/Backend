package com.example.olympicbe.controller;

import com.example.olympicbe.entity.Post;
import com.example.olympicbe.entity.User;
import com.example.olympicbe.repository.UserRepository;
import com.example.olympicbe.service.BookmarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "북마크")
@RestController
@RequestMapping("/members/me/bookmarks")
public class BookmarkController {

   private final BookmarkService bookmarkService;
   private final UserRepository userRepository;

   public BookmarkController(BookmarkService bookmarkService, UserRepository userRepository) {
      this.bookmarkService = bookmarkService;
      this.userRepository=userRepository;
   }

   @ApiOperation(value = "북마크 조회", notes = "This is a sample GET method")   // 북마크 조회
   @GetMapping
   public ResponseEntity<List<Post>> getBookmarks(Authentication authentication) {
      String userId = authentication.getName();
      User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

      List<Post> bookmarks = bookmarkService.getUserBookmarks(user.getId());
      return ResponseEntity.ok(bookmarks);
   }
   // 북마크 추가
   @PostMapping("/{postId}")
   public ResponseEntity<Void> addBookmark(@PathVariable Long postId, Authentication authentication) {
      String userId = authentication.getName();
      User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

      bookmarkService.addBookmark(user.getId(), postId);
      return ResponseEntity.ok().build();
   }

   // 북마크 삭제
   @DeleteMapping("/{postId}")
   public ResponseEntity<Void> removeBookmark(@PathVariable Long postId, Authentication authentication) {
      String userId = authentication.getName();
      User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

      bookmarkService.removeBookmark(user.getId(), postId);
      return ResponseEntity.ok().build();
   }
}
