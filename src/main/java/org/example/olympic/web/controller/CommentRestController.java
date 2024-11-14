package org.example.olympic.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.olympic.service.CommentService;
import org.example.olympic.web.dto.CommentRequestDTO;
import org.example.olympic.web.dto.CommentResponseDTO;
import org.example.olympic.web.dto.LikeDislikeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyMethod")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;
    @Operation(summary = "댓글 생성", description = "특정 공부법에 댓글을 작성합니다.")
    @PostMapping("/{studyId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(
            @Parameter(description = "댓글을 작성할 공부법의 ID", required = true)
            @PathVariable Long studyId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        commentRequestDTO.setStudyId(studyId); // studyId 설정
        CommentResponseDTO createdComment = commentService.createComment(commentRequestDTO);
        return ResponseEntity.ok(createdComment);
    }
    @Operation(summary = "댓글 조회", description = "특정 공부법에 달린 댓글들을 조회합니다.")
    @GetMapping("/{studyId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(
            @Parameter(description = "댓글을 조회할 공부법의 ID", required = true)
            @PathVariable Long studyId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByStudyId(studyId);
        return ResponseEntity.ok(comments);
    }
    @Operation(summary = "댓글 추천", description = "특정 댓글에 추천을 추가하고 최신 추천 및 비추천 개수를 반환합니다.")
    @PostMapping("/{commentId}/like")
    public ResponseEntity<LikeDislikeResponseDTO> likeComment(
            @PathVariable Long commentId) {
        LikeDislikeResponseDTO response = commentService.likeComment(commentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 비추천", description = "특정 댓글에 비추천을 추가하고 최신 추천 및 비추천 개수를 반환합니다.")
    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<LikeDislikeResponseDTO> dislikeComment(
            @PathVariable Long commentId) {
        LikeDislikeResponseDTO response = commentService.dislikeComment(commentId);
        return ResponseEntity.ok(response);
    }












    // 일단 보류
//    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
//    @DeleteMapping("/{studyId}/comments/{commentId}")
//    public ResponseEntity<Void> deleteComment(
//            @Parameter(description = "댓글을 삭제할 공부법의 ID", required = true)
//            @PathVariable Long studyId,
//            @Parameter(description = "삭제할 댓글의 ID", required = true)
//            @PathVariable Long commentId) {
//        commentService.deleteComment(studyId, commentId);
//        return ResponseEntity.noContent().build();
//    }
}

