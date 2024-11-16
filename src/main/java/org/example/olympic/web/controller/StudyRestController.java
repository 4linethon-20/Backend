package org.example.olympic.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.olympic.service.StudyService;
import org.example.olympic.web.dto.StudyRequestDTO;
import org.example.olympic.web.dto.StudyResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyMethod")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StudyRestController {
    private final StudyService studyService;
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "공부법 작성", description = "공부법 게시물 작성")
    public ResponseEntity<StudyResponseDTO> createStudy(
            @ModelAttribute StudyRequestDTO studyRequestDTO,
            Authentication authentication) {
        // 인증된 사용자의 ID를 가져옴
        String userId = authentication.getName();

        // 서비스 레이어에 인증된 사용자 ID 전달
        StudyResponseDTO studyResponseDTO = studyService.createStudy(studyRequestDTO, userId);
        return ResponseEntity.ok(studyResponseDTO);
    }

    // 공부법 수정 메서드
    @PutMapping("/{studyId}")
    @Operation(summary = "공부법 수정", description = "공부법 게시글 수정")
    public ResponseEntity<StudyResponseDTO> updateStudy(
            @PathVariable Long studyId,
            @RequestBody StudyRequestDTO studyRequestDTO) {
        // 서비스 메서드 호출
        StudyResponseDTO updatedStudy = studyService.updateStudy(studyId, studyRequestDTO);
        return ResponseEntity.ok(updatedStudy);
    }
    @GetMapping("/top3")
    @Operation(summary = "추천수 상위 3개 공부법 조회", description = "추천수가 가장 많은 상위 3개의 공부법을 조회")
    public ResponseEntity<List<StudyResponseDTO>> getTop3StudiesByLikes(){
        List<StudyResponseDTO> topStudies = studyService.getTop3StudiesByLikes();
        return ResponseEntity.ok(topStudies);
    }

    @Operation(summary = "최근 조회한 공부법 기반 3개 공부법 조회", description = "최근에 조회한 공부법의 해시태그를 기반으로 상위 3개의 관련 공부법을 조회합니다.")
    @GetMapping("/related-by-recent/{recentStudyId}")
    public ResponseEntity<List<StudyResponseDTO>> getRelatedStudiesByRecentStudy(
            @PathVariable Long recentStudyId) {
        List<StudyResponseDTO> studies = studyService.getRelatedStudiesByRecentStudy(recentStudyId);
        return ResponseEntity.ok(studies);
    }
    @Operation(summary = "관심있는 공부법 3개 조회", description = "회원이 관심 있는 과목을 기반으로 3개의 공부법을 조회합니다.")
    @GetMapping("/interested-subjects")
    public ResponseEntity<List<StudyResponseDTO>> getStudiesByUserInterestSubjects(@RequestParam Long memberId) {
        List<StudyResponseDTO> studies = studyService.getStudiesByUserInterestSubjects(memberId);
        return ResponseEntity.ok(studies);
    }
    @Operation(summary = "지금 인기있는 공부법 (오른쪽 UI)", description = "좋아요 수가 많은 상위 5개 공부법의 제목을 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<List<String>> getPopularStudies() {
        List<String> popularStudies = studyService.getTop5StudyTitlesByLikes();
        return ResponseEntity.ok(popularStudies);
    }

}

