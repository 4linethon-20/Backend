package com.example.olympicbe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public class JwtTokenFilter implements jakarta.servlet.Filter {

   private final JwtTokenProvider jwtTokenProvider;
   private final UserDetailsService userDetailsService;

   public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
      this.jwtTokenProvider = jwtTokenProvider;
      this.userDetailsService = userDetailsService; // UserDetailsService로 대체
   }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
         throws IOException, ServletException {

      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;

      try {
         String token = jwtTokenProvider.resolveToken(httpRequest);


         if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUserId(token);


            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            if (userDetails != null) {
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                     userDetails, null, userDetails.getAuthorities());
               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
               SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
               System.out.println("User details not found");
            }
         } else {
            System.out.println("Token is invalid or not found");
         }

      } catch (Exception e) {
         // 에러 로그 추가
         System.out.println("JWT 검증 중 오류 발생: " + e.getMessage());
      }

      filterChain.doFilter(httpRequest, httpResponse);
   }

}
