package org.example.olympicbe.service;

import lombok.RequiredArgsConstructor;
import org.example.olympicbe.domain.Member;
import org.example.olympicbe.domain.Study;
import org.example.olympicbe.repository.MemberRepository;
import org.example.olympicbe.repository.StudyRepository;
import org.example.olympicbe.web.dto.StudyRequestDTO;
import org.example.olympicbe.web.dto.StudyResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final String imagePath = "/images/studies/";
    public StudyResponseDTO createStudy(StudyRequestDTO studyRequestDTO) {
        Member member = memberRepository.findById(studyRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        String studyImageUrl = null;
        if (studyRequestDTO.getStudyImage() != null) {
            try {
                studyImageUrl = saveImage(studyRequestDTO.getStudyImage());
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리 로직 추가 (예: 기본 이미지 URL을 설정하거나 에러 메시지 반환)
            }
        }

        Study study = Study.builder()
                .title(studyRequestDTO.getTitle())
                .content(studyRequestDTO.getContent())
                .member(member)
                .hashtags(studyRequestDTO.getHashtags())
                .studyImageUrl(studyImageUrl)
                .build();

        Study savedStudy = studyRepository.save(study);
        return StudyResponseDTO.builder()
                .id(savedStudy.getId())
                .title(savedStudy.getTitle())
                .content(savedStudy.getContent())
                .hashtags(savedStudy.getHashtags())
                .studyImageUrl(savedStudy.getStudyImageUrl())
                .memberProfileImageUrl(member.getProfileImageUrl())
                .createdAt(savedStudy.getCreatedAt())
                .build();
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        // 파일 저장 로직 구현 (예: 로컬 파일 시스템 또는 AWS S3)
        // 여기서는 간단히 파일 경로를 리턴하는 예를 들겠습니다.
        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filepath = Paths.get(imagePath, filename);
        Files.copy(imageFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        return filepath.toString();
    }

    public StudyResponseDTO getStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        return StudyResponseDTO.builder()
                .id(study.getId())
                .title(study.getTitle())
                .likesCount(study.getLikesList().size())
                .commentCount(study.getCommentList().size())
                .content(study.getContent())
                .hashtags(study.getHashtags())
                .createdAt(study.getCreatedAt())
                .updatedAt(study.getUpdatedAt())
                .build();
    }

    public List<StudyResponseDTO> getTop3StudiesByLikes() {
        Pageable topThree = PageRequest.of(0, 3);
        return studyRepository.findTop3ByOrderByLikesCountDescAndCreatedAtDesc(topThree).stream()
                .map(study -> StudyResponseDTO.builder()
                        .id(study.getId())
                        .title(study.getTitle())
                        .likesCount(study.getLikesList().size())
                        .commentCount(study.getCommentList().size())
                        .content(study.getContent())
                        .hashtags(study.getHashtags()) // 해시태그 추가
                        .createdAt(study.getCreatedAt())
                        .updatedAt(study.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<StudyResponseDTO> getRelatedStudiesByRecentStudy(Long recentStudyId) {
        // 최근 조회한 공부법 조회
        Study recentStudy = studyRepository.findById(recentStudyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        // 최근 조회한 공부법의 해시태그와 동일한 해시태그를 가진 상위 3개의 공부법 조회
        List<Study> relatedStudies = studyRepository.findTop3ByHashtagsInAndIdNotOrderByCreatedAtDesc(
                recentStudy.getHashtags(), recentStudy.getId());

        // DTO로 변환하여 반환
        return relatedStudies.stream()
                .map(study -> StudyResponseDTO.builder()
                        .id(study.getId())
                        .title(study.getTitle())
                        .content(study.getContent())
                        .hashtags(study.getHashtags())
                        .likesCount(study.getLikesList().size())
                        .commentCount(study.getCommentList().size())
                        .createdAt(study.getCreatedAt())
                        .updatedAt(study.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    // 관심 있는 과목과 관련된 상위 3개의 공부법 조회
    public List<StudyResponseDTO> getStudiesByUserInterestSubjects(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        List<String> subjects = member.getSubjects();
        Pageable topThree = PageRequest.of(0, 3);

        // Repository 메서드 호출 시 Pageable 객체 전달
        List<Study> relatedStudies = studyRepository.findTopByHashtagsInSubjects(subjects, topThree);

        return relatedStudies.stream()
                .map(study -> StudyResponseDTO.builder()
                        .id(study.getId())
                        .title(study.getTitle())
                        .content(study.getContent())
                        .hashtags(study.getHashtags())
                        .likesCount(study.getLikesList().size())
                        .commentCount(study.getCommentList().size())
                        .createdAt(study.getCreatedAt())
                        .updatedAt(study.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    public List<String> getTop5StudyTitlesByLikes() {
        return studyRepository.findTop5ByOrderByLikesListDesc().stream()
                .map(Study::getTitle)  // 제목만 추출
                .collect(Collectors.toList());
    }
    /*오류로 후 순위로 미룸*/
    // 여러 관심 과목을 기준으로 상위 3개의 공부법 조회
//    public List<StudyResponseDTO> getStudiesByUserInterestSubjects(List<String> subjects) {
//        List<Study> relatedStudies = studyRepository.findTop3BySubjectInOrderByCreatedAtDesc(subjects);
//
//        return relatedStudies.stream()
//                .map(study -> StudyResponseDTO.builder()
//                        .id(study.getId())
//                        .title(study.getTitle())
//                        .content(study.getContent())
//                        .hashtags(study.getHashtags())
//                        .createdAt(study.getCreatedAt())
//                        .updatedAt(study.getUpdatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
    /*오류로 후 순위로 미룸*/

//    public List<StudyResponseDTO> getRelatedStudies(Long recentStudyId) {
//        // 최근에 조회한 공부법 조회
//        Study recentStudy = studyRepository.findById(recentStudyId)
//                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
//
//        // 해시태그를 기준으로 연관된 상위 3개의 공부법 조회
//        List<Study> relatedStudies = studyRepository.findTop3ByHashtagsInAndIdNotOrderByCreatedAtDesc(
//                recentStudy.getHashtags(), recentStudy.getId());
//
//        // DTO로 변환하여 반환
//        return relatedStudies.stream()
//                .map(study -> StudyResponseDTO.builder()
//                        .id(study.getId())
//                        .title(study.getTitle())
//                        .content(study.getContent())
//                        .hashtags(study.getHashtags())
//                        .createdAt(study.getCreatedAt())
//                        .updatedAt(study.getUpdatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }

}



