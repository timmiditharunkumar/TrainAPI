package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Configures security settings for HTTP requests.
	 * 
	 * @param http the HttpSecurity object used to configure security settings
	 * @return the SecurityFilterChain object configured with the specified settings
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				// Configure URL-based authorization
//				.authorizeHttpRequests(auth -> auth
//						// Only users with the ADMIN role can access endpoints under /trains
//						.requestMatchers(HttpMethod.DELETE, "/trains").hasRole("ADMIN")
//						.requestMatchers(HttpMethod.PUT, "/trains/{trainNumber}").hasRole("ADMIN")
//						// Allow all requests to the H2 console without authentication
//						.requestMatchers("/h2-console/**").permitAll()
//						.requestMatchers(HttpMethod.DELETE, "/trains/{trainNumber}").authenticated())
//				// Enable Basic Authentication for the application
//				.httpBasic().and()
//				// Disable CSRF protection for simplicity in this example
//				.csrf().disable()
//				// Disable frame options to allow embedding H2 console in an iframe
//				.headers().frameOptions().disable();

		http
        // Disable CSRF protection for simplicity in this example
        .csrf(csrf -> csrf.disable())
        
        // Disable frame options to allow embedding H2 console in an iframe
        .headers(headers -> headers.frameOptions().disable())
        
        // Configure URL-based authorization
        .authorizeHttpRequests(auth -> auth
            // Only users with the ADMIN role can access DELETE and PUT endpoints under /trains
        		.requestMatchers(HttpMethod.DELETE, "/trains").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/trains/{trainNumber}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/trains/{trainNumber}").hasRole("ADMIN")
            
            // Allow all requests to the H2 console without authentication
            .requestMatchers("/h2-console/**").permitAll()
            
            // Allow all other requests without authentication
            .anyRequest().permitAll()
        )
        
        // Enable Basic Authentication for the application
        .httpBasic();
		
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		// Create an admin user with ADMIN role
		UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN")
				.build();

		// Create a general user with USER role
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
				.build();

		return new InMemoryUserDetailsManager(admin, user);
	}
}
