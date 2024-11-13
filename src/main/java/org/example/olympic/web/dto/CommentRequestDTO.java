package org.example.olympic.web.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private String content;
    private Long studyId;
    private Long memberId;
}
