package org.example.olympic.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
   private String userId;
   private String password;
   private String nickname;
   private String profileImage;
   private List<String> subjects;
}