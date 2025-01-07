package com.shubh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

	@Bean
	public UserDetailsService userDetailService() {

		UserDetails user = User.builder().username("shubhamsroff321@gmail.com")
				.password(passwordEncoder().encode("shubh")).roles("ADMIN").build();
		UserDetails user2 = User.builder().username("ram@gmail.com").password(passwordEncoder().encode("ram"))
				.roles("ADMIN").build();
		return new InMemoryUserDetailsManager(user, user2);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
