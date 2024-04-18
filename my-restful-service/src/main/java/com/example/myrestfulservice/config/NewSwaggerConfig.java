package com.example.myrestfulservice.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(info = @Info(title = "My Restful SErvice API 명세서", description = "Spring Boot로 개발하는 Restful API 명세서 입니다.", version = "v.1.0.0"))
@Configuration
@RequiredArgsConstructor
public class NewSwaggerConfig {

	@Bean
	public GroupedOpenApi customTestOpenApi() {
		String[] paths = {"/users/**", "/admin/**"};
		
		return GroupedOpenApi.builder()
				.group("일반 사용자와 관리자를 위한 User 도메인에 대한 API")
				.pathsToMatch(paths)
				.build();
	}
}
