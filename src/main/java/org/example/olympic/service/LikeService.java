package org.example.olympic.service;

import lombok.RequiredArgsConstructor;
import org.example.olympic.domain.Likes;
import org.example.olympic.domain.User;
import org.example.olympic.domain.Study;
import org.example.olympic.repository.LikeRepository;
import org.example.olympic.repository.StudyRepository;
import org.example.olympic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    // 좋아요 추가
    public void addLike(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 좋아요 중복 방지
        if (!likeRepository.existsByStudyAndUser(study, user)) {
            Likes like = new Likes();
            like.setStudy(study);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    // 좋아요 취소
    @Transactional
    public void removeLike(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 좋아요 기록이 있는 경우에만 삭제
        if (likeRepository.existsByStudyAndUser(study, user)) {
            likeRepository.deleteByStudyAndUser(study, user);
        }
    }

    // 특정 공부법에 대한 좋아요 개수 조회
    public long getLikeCount(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        return likeRepository.countByStudy(study);
    }
}