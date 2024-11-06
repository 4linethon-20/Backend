package com.example.olympicbe.service;

import com.example.olympicbe.dto.UserDTO;
import com.example.olympicbe.entity.User;
import com.example.olympicbe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
      this.userRepository=userRepository;
      this.passwordEncoder=passwordEncoder;
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
         System.out.println("aleradyr: " );
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User ID already exists.");
      }

      System.out.println("Registering user: " + userDto.getUserId());
      User user = new User();
      user.setUserId(userDto.getUserId());
      user.setPassword(passwordEncoder.encode(userDto.getPassword()));
      user.setProfileImage(userDto.getProfileImage());
      user.setNickname(userDto.getNickname());
      user.setInterests(userDto.getInterests());

      return userRepository.save(user);
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

}
