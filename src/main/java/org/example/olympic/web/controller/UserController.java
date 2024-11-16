package org.example.olympic.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.example.olympic.domain.Subject;
import org.example.olympic.domain.User;
import org.example.olympic.dto.RegisterDTO;
import org.example.olympic.dto.UserDTO;
import org.example.olympic.security.JwtTokenProvider;
import org.example.olympic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class UserController {
   private final UserService userService;
   private final JwtTokenProvider jwtTokenProvider;
   private final AuthenticationManager authenticationManager;

   public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
      this.userService=userService;
      this.jwtTokenProvider=jwtTokenProvider;
      this.authenticationManager=authenticationManager;
   }
   @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<User> registerUser(
           @RequestPart("user") @Valid String userJson,
           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws JsonProcessingException {

      // JSON을 객체로 매핑
      ObjectMapper objectMapper = new ObjectMapper();
      RegisterDTO registerDto = objectMapper.readValue(userJson, RegisterDTO.class);
      // 디버깅 로그 추가
      if (profileImage != null) {
         System.out.println("File name: " + profileImage.getOriginalFilename());
         System.out.println("File size: " + profileImage.getSize());
      }
      User registeredUser = userService.registerUser(registerDto, profileImage);

      return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
   }



   @GetMapping("/checkId")
   public String checkUserId(@RequestParam UserDTO userId){
      boolean isAvalilable = userService.checkUserId(userId);
      if(isAvalilable)
         return "사용 가능한 아이디 입니다.";
      else return "이미 존재하는 아이디 입니다.";
   }

   @PostMapping("/login")
   public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO userDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
      try {
         // 사용자 인증
         Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(userDto.getUserId(), userDto.getPassword())
         );

         // 인증 성공 시 JWT 토큰 생성
         String jwtToken = jwtTokenProvider.createToken(authentication.getName());

         // 토큰을 Map 형태로 반환
         Map<String, String> response = new HashMap<>();
         response.put("token", jwtToken);

         return ResponseEntity.ok(response);
      } catch (AuthenticationException ex) {
         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
      }
   }
   //마이페이지 조회
   @GetMapping("/me")
   public ResponseEntity<UserDTO> getMyPage(Authentication authentication){
      String userId= authentication.getName();
      UserDTO response = userService.MyPage(userId);
      return ResponseEntity.ok(response);
   }


   @GetMapping("/search")
   public ResponseEntity<List<Subject>> searchSubjects(@RequestParam("keyword") String keyword) {
      List<Subject> subjects = userService.searchSubjects(keyword);
      return ResponseEntity.ok(subjects);
   }
}
