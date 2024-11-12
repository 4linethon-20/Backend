package com.example.olympicbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

   @Bean
   public Docket api() {
      return new Docket(DocumentationType.OAS_30) // OpenAPI 3.0
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.olympicbe.controller")) // 컨트롤러가 있는 패키지
            .paths(PathSelectors.any()) // 모든 경로 문서화
            .build();
   }
}