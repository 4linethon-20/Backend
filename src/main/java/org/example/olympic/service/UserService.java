package org.example.olympic.service;


import org.example.olympic.domain.Subject;
import org.example.olympic.domain.User;
import org.example.olympic.dto.UserDTO;
import org.example.olympic.repository.SubjectRepository;
import org.example.olympic.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
   public User registerUser(UserDTO userDto){
      if(userRepository.existsByUserId(userDto.getUserId())){
         System.out.println("already registered: " );
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User ID already exists.");
      }

      User user = new User();
      user.setUserId(userDto.getUserId());
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
      user.setProfileImageUrl(userDto.getProfileImage());
      user.setNickname(userDto.getNickname());

      List<Subject> subjects = userDto.getSubjects().stream()
            .map(subjectName -> subjectRepository.findBySubjectNameContainingIgnoreCase(subjectName))
                  .flatMap(List::stream).collect(Collectors.toList());

      return userRepository.save(user);
   }
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
   //마이페이지 조회 기능
   public User MyPage(String userId){
      return userRepository.findByUserId(userId)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
   }

}
