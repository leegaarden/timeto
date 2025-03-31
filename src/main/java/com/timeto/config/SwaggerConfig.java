package com.timeto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Development Server");

        // OAuth2 보안 스키마 정의
        SecurityScheme oauth2Scheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .implicit(new OAuthFlow()
                                .authorizationUrl("/oauth2/authorization/google")
                                .scopes(new Scopes()
                                        .addString("email", "이메일 정보 접근")
                                        .addString("profile", "프로필 정보 접근"))
                        ));

        return new OpenAPI()
                .servers(List.of(localServer))
                .components(new Components()
                        .addSecuritySchemes("oauth2", oauth2Scheme))
                .info(new Info()
                        .title("TimeTO API Documentation")
                        .version("1.0")
                        .description("TimeTO 시간 관리 서비스의 API 문서입니다."))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }
}