package com.nationstates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.builder().username("user").password("{noop}userpassword").roles("USER").build();
		UserDetails editor = User.builder().username("editor").password("{noop}editorpassword").roles("EDITOR").build();

		return new InMemoryUserDetailsManager(user, editor);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(req -> req.requestMatchers("/country/**").hasRole("EDITOR")
				.requestMatchers(HttpMethod.PUT, "/cities/**").hasRole("EDITOR")
				.requestMatchers(HttpMethod.POST, "/cities/**").hasRole("EDITOR")
				.anyRequest().authenticated())
				.httpBasic();

		return http.csrf().disable().build();
	}
}
