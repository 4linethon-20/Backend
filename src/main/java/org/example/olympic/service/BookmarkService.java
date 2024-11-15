package org.example.olympic.service;

import org.example.olympic.domain.Bookmark;
import org.example.olympic.domain.Study;
import org.example.olympic.domain.User;
import org.example.olympic.repository.BookmarkRepository;
import org.example.olympic.repository.StudyRepository;
import org.example.olympic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, StudyRepository studyRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.studyRepository = studyRepository;
    }

    public List<Study> getUserBookmarks(Long userId) {
        User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user); // findByUser() 사용
        return bookmarks.stream()
              .map(Bookmark::getStudy) // Bookmark에서 Study로 매핑
              .collect(Collectors.toList());
    }

    public void addBookmark(Long userId, Long postId) {
        User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        Study study = studyRepository.findById(postId)
              .orElseThrow(() -> new RuntimeException("Study not found"));

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserAndStudy(user, study); // 수정
        if (existingBookmark.isPresent()) {
            throw new RuntimeException("This post is already bookmarked by the user.");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setStudy(study);

        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long userId, Long postId) {
        User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

        Study study = studyRepository.findById(postId)
              .orElseThrow(() -> new RuntimeException("Study not found"));

        Bookmark bookmark = bookmarkRepository.findByUserAndStudy(user, study)  // 수정
              .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);
    }

}


