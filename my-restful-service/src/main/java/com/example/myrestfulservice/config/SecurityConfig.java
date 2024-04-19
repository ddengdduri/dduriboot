package com.example.myrestfulservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

		UserDetails newUser = User.withUsername("user").password(passwordEncoder().encode("passw0rd"))
				.authorities("read").build();

		userDetailsManager.createUser(newUser);
		return userDetailsManager;

	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}


/*
 * 리소스의 변경을 가져오는 DELETE, CREATE같은 UPDATE 작업을 사용하기 위해서는 SecurityFilterChain을 Bean으로 등록해야 오류없이 처리 가능
 * */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector handlerMappingIntrospector)
			throws Exception {
		/*
		 * http Security 객체에서는 CSRF 에 대한 설정을 허용할 수 있는 옵션을 지정
		 */
		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();

	}
}
