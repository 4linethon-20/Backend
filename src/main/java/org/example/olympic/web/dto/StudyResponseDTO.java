package org.example.olympic.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Getter
@Setter
public class StudyResponseDTO {
    private Long id;
    private String title;
    private String content;
    private List<String> hashtags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private int commentCount;
    //private String studyImageUrl;
    //private String memberProfileImageUrl;
}
