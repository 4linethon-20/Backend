package org.example.olympic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

   @Bean
   public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
         @Override
         public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                  // 모든 오리진 허용
                  .allowedOriginPatterns("*")
                  // 허용할 HTTP 메서드
                  .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                  // 허용할 헤더
                  .allowedHeaders("*")
                  // 자격 증명(쿠키, 인증 정보) 허용
                  .allowCredentials(true)
                  // 캐시 시간 설정 (초 단위)
                  .maxAge(3600);
         }
      };
   }
}
