package org.example.olympicbe.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikeDislikeResponseDTO {
    private int likeCount;
    private int dislikeCount;
}
