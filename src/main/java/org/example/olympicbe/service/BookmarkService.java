package org.example.olympicbe.service;

import lombok.RequiredArgsConstructor;
import org.example.olympicbe.domain.Bookmark;
import org.example.olympicbe.domain.Member;
import org.example.olympicbe.domain.Study;
import org.example.olympicbe.repository.BookmarkRepository;
import org.example.olympicbe.repository.MemberRepository;
import org.example.olympicbe.repository.StudyRepository;
import org.example.olympicbe.web.dto.StudyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    // 북마크 추가
    public void addBookmark(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 북마크 중복 방지
        if (!bookmarkRepository.existsByStudyAndMember(study, member)) {
            Bookmark bookmark = new Bookmark();
            bookmark.setStudy(study);
            bookmark.setMember(member);
            bookmarkRepository.save(bookmark);
        }
    }

    // 북마크 삭제
    @Transactional
    public void removeBookmark(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 북마크 기록이 있는 경우에만 삭제
        if (bookmarkRepository.existsByStudyAndMember(study, member)) {
            bookmarkRepository.deleteByStudyAndMember(study, member);
        }
    }

    // 특정 사용자의 북마크 목록 조회
    public List<StudyResponseDTO> getBookmarks(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return bookmarkRepository.findByMember(member).stream()
                .map(bookmark -> {
                    Study study = bookmark.getStudy();
                    return StudyResponseDTO.builder()
                            .id(study.getId())
                            .title(study.getTitle())
                            .content(study.getContent())
                            .hashtags(study.getHashtags())
                            .createdAt(study.getCreatedAt())
                            .updatedAt(study.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

