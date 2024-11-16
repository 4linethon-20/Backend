package org.example.olympic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class RegisterDTO {
   private String userId;
   private String password;
   private String nickname;
   private List<String> subjects;

}