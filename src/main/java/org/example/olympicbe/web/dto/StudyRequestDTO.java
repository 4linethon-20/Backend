package org.example.olympicbe.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
public class StudyRequestDTO {
    private String title;
    private String content;
    private List<String> hashtags;
    private Long memberId;
    private MultipartFile studyImage;
}
