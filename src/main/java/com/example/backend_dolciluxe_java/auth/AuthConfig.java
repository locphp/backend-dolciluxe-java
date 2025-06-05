// package com.example.backend_dolciluxe_java.auth;

// import com.example.backend_dolciluxe_java.auth.filter.JwtAuthFilter;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import
// org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import
// org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import
// org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @Configuration
// @EnableWebSecurity
// public class AuthConfig {

// @Autowired
// private JwtAuthFilter jwtAuthFilter;

// @Autowired
// private UserDetailsService userDetailsService;

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// .csrf(AbstractHttpConfigurer::disable)
// .authorizeHttpRequests((authorize) -> authorize
// .requestMatchers("/api/auth/register", "/api/auth/login",
// "/api/auth/refresh-token")
// .permitAll()
// .requestMatchers("/api/auth/logout").authenticated()
// .anyRequest().authenticated())
// .sessionManagement((session) -> session
// .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// .authenticationProvider(authenticationProvider())
// .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// @Bean
// public AuthenticationProvider authenticationProvider() {
// DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
// authProvider.setUserDetailsService(userDetailsService);
// authProvider.setPasswordEncoder(passwordEncoder());
// return authProvider;
// }

// @Bean
// public AuthenticationManager
// authenticationManager(AuthenticationConfiguration config) throws Exception {
// return config.getAuthenticationManager();
// }

// @Bean
// public CorsConfigurationSource corsConfigurationSource() {
// CorsConfiguration config = new CorsConfiguration();
// config.setAllowedOrigins(List.of("http://localhost:5173"));
// config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
// config.setAllowedHeaders(List.of("*"));
// config.setAllowCredentials(true); // Nếu cần gửi Authorization header

// UrlBasedCorsConfigurationSource source = new
// UrlBasedCorsConfigurationSource();
// source.registerCorsConfiguration("/**", config);
// return source;
// }
// }