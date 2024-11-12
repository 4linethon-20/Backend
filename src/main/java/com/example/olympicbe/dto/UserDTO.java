package com.example.olympicbe.dto;

import com.example.olympicbe.entity.Subject;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
   private String userId;
   private String password;
   private String nickname;
   private String profileImage;
   private Set<Subject> subjects;
}
