package com.jprince.jwt.jwt.test.config;

import org.springframework.context.annotation.Bean;
import com.jprince.jwt.jwt.test.service.JwtService;
import com.jprince.jwt.jwt.test.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jprince.jwt.jwt.test.filter.JwtAuthenticationFIlter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final UserDetailsServiceImp userDetailsServiceImp;
	private final JwtAuthenticationFIlter jwtAuthenticationFIlter;
	
	public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,JwtAuthenticationFIlter jwtAuthenticationFIlter) {
		this.jwtAuthenticationFIlter = jwtAuthenticationFIlter;
		this.userDetailsServiceImp = userDetailsServiceImp;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						req -> req.requestMatchers("/login/**","/register/**")
						.permitAll()
						.anyRequest()
						.authenticated()
						).userDetailsService(userDetailsServiceImp)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFIlter,UsernamePasswordAuthenticationFilter.class)
				.build();
		}
		@Bean
		public PasswordEncoder passwordEncoder()
		{
			return new BCryptPasswordEncoder();
		}
				
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
		{
			return configuration.getAuthenticationManager();
		}
	}
	

