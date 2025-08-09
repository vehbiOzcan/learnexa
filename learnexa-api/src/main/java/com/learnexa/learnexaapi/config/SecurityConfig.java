package com.learnexa.learnexaapi.config;

import com.learnexa.learnexaapi.jwt.AuthEntryPoint;
import com.learnexa.learnexaapi.jwt.JwtAuthenticationFilter;
import com.learnexa.learnexaapi.entity.enums.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String AUTHENTICATE = "/api/auth/authenticate";
    public static final String REGISTER = "/api/auth/register";
    public static final String REFRESH_TOKEN = "/api/auth/refresh-token";
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**"
    };
    public static final String ADMIN = "/api/admin/**";
    public static final String USER = "/api/user/**";

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins - development için geniş tutalım
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Özel CORS konfigürasyonu
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN).permitAll()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                //.requestMatchers("/api/uploads/**").permitAll()
                                .requestMatchers(ADMIN).hasAuthority(Role.ADMIN.name())
                                .requestMatchers(USER).hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                                .anyRequest().authenticated())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}