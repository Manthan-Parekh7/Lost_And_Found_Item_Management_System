package com.ce.LostAndFoundManagement.LostAndFoundManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Modify query to retrieve user credentials from the 'users' table
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, true FROM users WHERE email=?");

        // Modify query to retrieve roles from the 'users' table
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT email, role FROM users WHERE email=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/signup").permitAll()

                        // User Specific Access
                        .requestMatchers(HttpMethod.GET, "/api/claims/matching").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/lost-items").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/found-items").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/claims").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/claims/user/{userId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/claims/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/lost-items/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/found-items/{id}").hasRole("USER")

                        // User and Admin Access
                        .requestMatchers(HttpMethod.POST, "/api/lost-items", "/api/found-items").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/lost-items/**", "/api/found-items/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("USER", "ADMIN")

                        //  Admin-only access

                        .requestMatchers(HttpMethod.PATCH, "/api/claims/{id}/status/{status}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/claims").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}","/api/lost-items/{id}", "/api/found-items/{id}", "/api/claims/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/most-reports").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/email/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/claims/**").hasRole("ADMIN")
        );

        // Use HTTP Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        // Disable CSRF for API security
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



