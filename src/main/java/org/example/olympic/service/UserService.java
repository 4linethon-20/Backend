package org.example.olympic.service;


import org.example.olympic.domain.Subject;
import org.example.olympic.domain.User;
import org.example.olympic.dto.RegisterDTO;
import org.example.olympic.dto.StudyDTO;
import org.example.olympic.dto.UserDTO;
import org.example.olympic.repository.SubjectRepository;
import org.example.olympic.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

   private final UserRepository userRepository;
   private final SubjectRepository subjectRepository;
   private final PasswordEncoder passwordEncoder;


   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SubjectRepository subjectRepository){
      this.userRepository=userRepository;
      this.passwordEncoder=passwordEncoder;
      this.subjectRepository=subjectRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
      User user = userRepository.findByUserId(userId)
            .orElseThrow(()->new UsernameNotFoundException("User not found with ID: "+userId));

      return org.springframework.security.core.userdetails.User
            .withUsername(user.getUserId())
            .password(user.getPassword())
            .authorities("USER")
            .build();

   }

   //회원가입 기능
   public User registerUser(RegisterDTO userDto, MultipartFile profileImage){
      if(userRepository.existsByUserId(userDto.getUserId())){
         System.out.println("already registered: " );
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User ID already exists.");
      }

      User user = new User();
      user.setUserId(userDto.getUserId());
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
      user.setProfileImageUrl(saveProfileImage(profileImage));
      user.setNickname(userDto.getNickname());

      List<Subject> subjects = userDto.getSubjects().stream()
            .map(subjectName -> subjectRepository.findBySubjectNameContainingIgnoreCase(subjectName))
                  .flatMap(List::stream).collect(Collectors.toList());

      return userRepository.save(user);
   }

   public String saveProfileImage(MultipartFile profileImage) {
      if (profileImage.isEmpty()) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile image is required.");
      }

      try {
         // 파일 저장 경로 설정
         String uploadDir = "uploads/profile-images/";
         String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
         Path filePath = Paths.get(uploadDir, fileName);

         // 파일 저장
         Files.createDirectories(filePath.getParent()); // 디렉토리 생성
         profileImage.transferTo(filePath.toFile());

         // 저장된 파일의 경로 반환 (예: http://localhost:8080/uploads/profile-images/{fileName})
         return "/uploads/profile-images/" + fileName;
      } catch (IOException e) {
         throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save profile image.");
      }
   }


   // 과목 검색
   public List<Subject> searchSubjects(String keyword) {
      return subjectRepository.findBySubjectNameContainingIgnoreCase(keyword);
   }
   //아이디 중복 체크 기능
   public boolean checkUserId(UserDTO userDto){
      return !userRepository.existsByUserId(userDto.getUserId());
   }
   //로그인 기능
   public User loginUser(String userId, String password){
      User user = userRepository.findByUserId(userId)
            .orElseThrow(()-> new UsernameNotFoundException("User not found with ID: "+userId));
      if(!passwordEncoder.matches(password, user.getPassword())){
         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password.");
      }
      return user;
   }

   public UserDTO MyPage(String userId) {
      User user = userRepository.findByUserIdWithStudies(userId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

      List<StudyDTO> studies = user.getStudyList().stream()
              .map(study -> new StudyDTO(
                      study.getId(),
                      study.getTitle(),
                      study.getContent(),
                      study.getStudyImageUrl(),
                      study.getCreatedAt(),
                      study.getUpdatedAt()))
              .toList();

      return new UserDTO(
              user.getUserId(),
              user.getPassword(),
              user.getNickname(),
              user.getProfileImageUrl(),
              user.getSubjects(),
              studies
      );
   }

}
