package com.example.olympicbe.dto;

import lombok.Data;

@Data
public class BookmarkDTO {
   private Long userId;
   private Long postId;
   private String createdAt;
}
