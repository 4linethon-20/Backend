package com.example.olympicbe.service;

import com.example.olympicbe.entity.Bookmark;
import com.example.olympicbe.entity.Post;
import com.example.olympicbe.entity.User;
import com.example.olympicbe.repository.BookmarkRepository;
import com.example.olympicbe.repository.PostRepository;
import com.example.olympicbe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

   private final BookmarkRepository bookmarkRepository;
   private final UserRepository userRepository;
   private final PostRepository postRepository;

   public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, PostRepository postRepository) {
      this.bookmarkRepository = bookmarkRepository;
      this.userRepository = userRepository;
      this.postRepository = postRepository;
   }

   public List<Post> getUserBookmarks(Long userId) {
      List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);
      return bookmarks.stream()
            .map(Bookmark::getPost)  // Bookmark에서 Post로 매핑
            .toList();
   }
   public void addBookmark(Long userId, Long postId) {
      User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

      Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

      Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);
      if (existingBookmark.isPresent()) {
         throw new RuntimeException("This post is already bookmarked by the user.");
      }
      Bookmark bookmark = new Bookmark();
      bookmark.setUser(user);
      bookmark.setPost(post);

      bookmarkRepository.save(bookmark);
   }

   public void removeBookmark(Long userId, Long postId) {
      Bookmark bookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new RuntimeException("Bookmark not found"));

      bookmarkRepository.delete(bookmark);
   }
}
