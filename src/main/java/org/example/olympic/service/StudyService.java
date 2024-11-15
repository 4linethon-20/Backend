package org.example.olympic.service;

import lombok.RequiredArgsConstructor;
import org.example.olympic.domain.User;
import org.example.olympic.domain.Study;
import org.example.olympic.repository.StudyRepository;
import org.example.olympic.repository.UserRepository;
import org.example.olympic.web.dto.StudyRequestDTO;
import org.example.olympic.web.dto.StudyResponseDTO;
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
    private final UserRepository userRepository;
    public StudyResponseDTO createStudy(StudyRequestDTO studyRequestDTO, String userId) {
        // userId를 사용하여 인증된 사용자 데이터를 처리
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Study study = Study.builder()
                .title(studyRequestDTO.getTitle())
                .content(studyRequestDTO.getContent())
                .user(user)
                .hashtags(studyRequestDTO.getHashtags())
                .build();

        Study savedStudy = studyRepository.save(study);
        return StudyResponseDTO.builder()
                .id(savedStudy.getId())
                .title(savedStudy.getTitle())
                .content(savedStudy.getContent())
                .hashtags(savedStudy.getHashtags())
//                .memberProfileImageUrl(user.getProfileImageUrl())
                .createdAt(savedStudy.getCreatedAt())
                .build();
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
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        List<String> subjects = user.getSubjects();
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



