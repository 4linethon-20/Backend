package org.example.olympicbe.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.olympicbe.service.BookmarkService;
import org.example.olympicbe.web.dto.StudyResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyMethod/{studyId}/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 추가", description = "특정 공부법을 북마크로 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> addBookmark(
            @PathVariable Long studyId,
            @RequestParam Long memberId) {
        bookmarkService.addBookmark(studyId, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "북마크 삭제", description = "특정 공부법에 대한 북마크를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<Void> removeBookmark(
            @PathVariable Long studyId,
            @RequestParam Long memberId) {
        bookmarkService.removeBookmark(studyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "북마크 목록 조회", description = "사용자가 북마크한 공부법 목록을 조회합니다.")
    @GetMapping("/members/me/bookmarks")
    public ResponseEntity<List<StudyResponseDTO>> getBookmarks(
            @RequestParam Long memberId) {
        List<StudyResponseDTO> bookmarks = bookmarkService.getBookmarks(memberId);
        return ResponseEntity.ok(bookmarks);
    }

}
