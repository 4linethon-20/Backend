package org.example.olympic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.olympic.domain.Study;
import org.example.olympic.domain.Subject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
   private String userId;
   private String password;
   private String nickname;
   private String profileImage;
   private List<String> subjects;
   private List<StudyDTO> studies;
}
