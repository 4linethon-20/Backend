package org.example.olympicbe.service;

import lombok.RequiredArgsConstructor;
import org.example.olympicbe.domain.Likes;
import org.example.olympicbe.domain.Member;
import org.example.olympicbe.domain.Study;
import org.example.olympicbe.repository.LikeRepository;
import org.example.olympicbe.repository.MemberRepository;
import org.example.olympicbe.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    // 좋아요 추가
    public void addLike(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 좋아요 중복 방지
        if (!likeRepository.existsByStudyAndMember(study, member)) {
            Likes like = new Likes();
            like.setStudy(study);
            like.setMember(member);
            likeRepository.save(like);
        }
    }

    // 좋아요 취소
    @Transactional
    public void removeLike(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 좋아요 기록이 있는 경우에만 삭제
        if (likeRepository.existsByStudyAndMember(study, member)) {
            likeRepository.deleteByStudyAndMember(study, member);
        }
    }

    // 특정 공부법에 대한 좋아요 개수 조회
    public long getLikeCount(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        return likeRepository.countByStudy(study);
    }
}