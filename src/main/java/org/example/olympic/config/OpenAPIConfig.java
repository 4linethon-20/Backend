package org.example.olympic.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API Documentation", version = "v1"),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = @Server(url = "http://localhost:8080") // API 서버 URL
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {

//   @Bean
//   public OpenAPI customOpenAPI() {
//      // JSON 데이터 정의
//      Schema<?> userSchema = new ObjectSchema()
//              .addProperty("userId", new Schema<>().type("string"))
//              .addProperty("password", new Schema<>().type("string"))
//              .addProperty("nickname", new Schema<>().type("string"))
//              .addProperty("subjects", new Schema<>().type("array").items(new Schema<>().type("string")));
//
//      // multipart/form-data 정의
//      Schema<?> multipartSchema = new ObjectSchema()
//              .addProperty("user", new Schema<>().type("string").example("{\"userId\": \"john\", \"password\": \"1234\"}")) // JSON으로 처리
//              .addProperty("profileImage", new Schema<>().type("string").format("binary"));
//
//      return new OpenAPI()
//              .components(new Components()
//                      .addRequestBodies("RegisterRequestBody",
//                              new RequestBody()
//                                      .content(new io.swagger.v3.oas.models.media.Content()
//                                              .addMediaType("multipart/form-data",
//                                                      new MediaType().schema(multipartSchema)))));
//   }

}
