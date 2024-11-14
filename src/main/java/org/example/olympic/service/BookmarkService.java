package org.example.olympic.service;

import lombok.RequiredArgsConstructor;
import org.example.olympic.domain.Bookmark;
import org.example.olympic.domain.User;
import org.example.olympic.domain.Study;
import org.example.olympic.repository.BookmarkRepository;
import org.example.olympic.repository.StudyRepository;
import org.example.olympic.repository.UserRepository;
import org.example.olympic.web.dto.StudyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    // 북마크 추가
    public void addBookmark(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 북마크 중복 방지
        if (!bookmarkRepository.existsByStudyAndUser(study, user)) {
            Bookmark bookmark = new Bookmark();
            bookmark.setStudy(study);
            bookmark.setUser(user);
            bookmarkRepository.save(bookmark);
        }
    }

    // 북마크 삭제
    @Transactional
    public void removeBookmark(Long studyId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 북마크 기록이 있는 경우에만 삭제
        if (bookmarkRepository.existsByStudyAndUser(study, user)) {
            bookmarkRepository.deleteByStudyAndUser(study, user);
        }
    }

    // 특정 사용자의 북마크 목록 조회
    public List<StudyResponseDTO> getBookmarks(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return bookmarkRepository.findByUser(user).stream()
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

