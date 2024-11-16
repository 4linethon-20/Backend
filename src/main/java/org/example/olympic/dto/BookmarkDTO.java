package org.example.olympic.dto;

import lombok.Data;
import org.example.olympic.domain.Bookmark;

@Data
public class BookmarkDTO {
   private long id;
   private Long userId;
   private String study;
   private String createdAt;
   public BookmarkDTO(Bookmark bookmark) {
      this.id = bookmark.getId();
      this.study = bookmark.getStudy().getTitle();
   }
}
