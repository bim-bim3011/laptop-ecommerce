package com.se1906.laptopshop.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {


     final String[] PUBLIC_ENDPOINTS={
             "/auth/**",
             "/home/**"

     };

     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

         http

                 .authorizeHttpRequests(auth -> auth
                         .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                         .requestMatchers("/admin/**").hasRole("ADMIN")

                         .anyRequest().authenticated()
                 );





         return http.build();
     }






    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
