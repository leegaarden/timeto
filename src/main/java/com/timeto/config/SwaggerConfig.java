package com.timeto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .openapi("3.0.1")  // 명시적으로 OpenAPI 버전 지정
                .info(new Info()
                        .title("TimeTO API")
                        .version("v1")
                        .description("TimeTO 애플리케이션 API 문서"));
    }
}