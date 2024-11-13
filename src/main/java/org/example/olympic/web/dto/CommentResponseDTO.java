package org.example.olympic.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDTO {
    private String studyTitle;
    private String studyContent;
    private Long id;
    private String nickname;
    private String content;
    private int likeCount;
    private int dislikeCount;
}
