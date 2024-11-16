package org.example.olympic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudyDTO {
   private Long id;
   private String title;
   private String content;
   private String imageUrl;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
}