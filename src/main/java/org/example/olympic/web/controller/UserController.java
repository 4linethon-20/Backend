package org.example.olympic.web.controller;

import org.example.olympic.domain.Subject;
import org.example.olympic.domain.User;
import org.example.olympic.dto.UserDTO;
import org.example.olympic.security.JwtTokenProvider;
import org.example.olympic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
   @PostMapping("/register")
   public ResponseEntity<User> registerUser(@RequestBody UserDTO userDto){
      User registeredUser = userService.registerUser(userDto);
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
   public ResponseEntity<User> getMyPage(Authentication authentication){
      String userId= authentication.getName();
      User user = userService.MyPage(userId);
      return ResponseEntity.ok(user);
   }


   @GetMapping("/search")
   public ResponseEntity<List<Subject>> searchSubjects(@RequestParam("keyword") String keyword) {
      List<Subject> subjects = userService.searchSubjects(keyword);
      return ResponseEntity.ok(subjects);
   }
}
