package org.example.olympic.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.olympic.service.StudyService;
import org.example.olympic.web.dto.StudyRequestDTO;
import org.example.olympic.web.dto.StudyResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyMethod")
@RequiredArgsConstructor
public class StudyRestController {
    private final StudyService studyService;
    @PostMapping
    @Operation(summary = "공부법 작성", description = "공부법 게시물 작성")
    public ResponseEntity<StudyResponseDTO> createStudy(@RequestBody StudyRequestDTO studyRequestDTO) {
    StudyResponseDTO studyResponseDTO = studyService.createStudy(studyRequestDTO);
    return ResponseEntity.ok(studyResponseDTO);
    }
    @Operation(summary = "공부법 조회", description = "특정 공부법의 기본 정보를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyResponseDTO> getStudy(
            @Parameter(description = "조회할 공부법의 ID", required = true)
            @PathVariable Long studyId) {
        StudyResponseDTO studyResponse = studyService.getStudy(studyId);
        return ResponseEntity.ok(studyResponse);
    }
    @GetMapping("/top3")
    @Operation(summary = "추천수 상위 3개 공부법 조회", description = "추천수가 가장 많은 상위 3개의 공부법을 조회")
    public ResponseEntity<List<StudyResponseDTO>> getTop3StudiesByLikes(){
        List<StudyResponseDTO> topStudies = studyService.getTop3StudiesByLikes();
        return ResponseEntity.ok(topStudies);
    }
//    @Operation(summary = "사용자 관심 과목 기반 3개 공부법 조회", description = "회원가입 시 입력한 여러 관심 과목을 기반으로 상위 3개의 공부법을 조회합니다.")
//    @GetMapping("/related-by-subjects")
//    public ResponseEntity<List<StudyResponseDTO>> getStudiesByUserInterestSubjects(
//            @RequestParam List<String> subjects) {
//        List<StudyResponseDTO> studies = studyService.getStudiesByUserInterestSubjects(subjects);
//        return ResponseEntity.ok(studies);
//    }

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

    //******************* 회원가입 연결해보고 마저 구현해보기 !! **********************
//    @GetMapping("/interested")
//    @Operation(summary = "관심 있는 과목의 공부법 조회", description = "유저가 관심 있는 과목과 관련된 상위 3개의 공부법을 조회")
//    public ResponseEntity<List<StudyResponseDTO>> getSstudiesByInterstedSubject(@RequestParam Long memberId){
//        List<StudyResponseDTO> studiesBySubject = studyService.getStudiesByInterestedSubject(memberId);
//        return ResponseEntity.ok(studiesBySubject);
//    }
//    @GetMapping("/related")
//    @Operation(summary = "최근에 조회한 공부법과 관련된 공부법 조회", description = "최근에 조회한 공부법과 동일한 주제의 상위 3개의 공부법을 조회합니다.")
//    public ResponseEntity<List<StudyResponseDTO>> getRelatedStudies(@RequestParam Long recentStudyId) {
//        List<StudyResponseDTO> relatedStudies = studyService.getRelatedStudies(recentStudyId);
//        return ResponseEntity.ok(relatedStudies);
//    }
}

