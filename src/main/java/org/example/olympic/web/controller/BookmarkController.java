package org.example.olympic.web.controller;

import org.example.olympic.domain.User;
import org.example.olympic.dto.BookmarkDTO;
import org.example.olympic.repository.UserRepository;
import org.example.olympic.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/members/me/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserRepository userRepository;

    public BookmarkController(BookmarkService bookmarkService, UserRepository userRepository) {
        this.bookmarkService = bookmarkService;
        this.userRepository=userRepository;
    }

    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> getBookmarks(Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findByUserId(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        List<BookmarkDTO> bookmarks = bookmarkService.getUserBookmarks(user.getId());
        return ResponseEntity.ok(bookmarks);
    }
    // 북마크 추가
    @PostMapping("/{studyId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Long studyId, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findByUserId(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        bookmarkService.addBookmark(user.getId(), studyId); // studyId로 변경
        return ResponseEntity.ok().build();
    }
    // 북마크 삭제
    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long studyId, Authentication authentication) {
        String userId = authentication.getName();
        User user = userRepository.findByUserId(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        bookmarkService.removeBookmark(user.getId(), studyId); // studyId로 변경
        return ResponseEntity.ok().build();
    }
}
