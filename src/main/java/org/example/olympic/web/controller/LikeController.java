package org.example.olympic.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.olympic.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studyMethod/{studyId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likesService;

    @Operation(summary = "좋아요 추가", description = "특정 공부법에 좋아요를 추가합니다.")
    @PostMapping("/add")
    public ResponseEntity<Void> addLike(
            @PathVariable Long studyId,
            @RequestParam Long memberId) {
        likesService.addLike(studyId, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "좋아요 취소", description = "특정 공부법에 대한 좋아요를 취소합니다.")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long studyId,
            @RequestParam Long memberId) {
        likesService.removeLike(studyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "좋아요 개수 조회", description = "특정 공부법에 대한 좋아요 개수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long studyId) {
        long likeCount = likesService.getLikeCount(studyId);
        return ResponseEntity.ok(likeCount);
    }
}

