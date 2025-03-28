package com.fundmate.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private final UserDetailsService userDetailsService;
//    private final JwtFilter jwtFilter;

//    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtFilter = jwtFilter;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**", "/api/products/**", "/api/reviews/**").permitAll()
//                        .requestMatchers("/api/users/**", "/api/orders/**").hasRole("CUSTOMER")
//                        .requestMatchers("/api/admin/orders/**").hasAnyRole("ADMIN", "AGENT")
//                        .requestMatchers("/api/admin/products/**").hasRole("ADMIN")
//                        .requestMatchers("/api/admin/users/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                // .httpBasic(Customizer.withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}