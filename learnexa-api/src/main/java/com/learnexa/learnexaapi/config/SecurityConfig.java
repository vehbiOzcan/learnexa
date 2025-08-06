package com.learnexa.learnexaapi.config;

import com.learnexa.learnexaapi.jwt.AuthEntryPoint;
import com.learnexa.learnexaapi.jwt.JwtAuthenticationFilter;
import com.learnexa.learnexaapi.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
        .authorizeHttpRequests(request ->
                request
                        .requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        //.antMatchers("/api/uploads/**").permitAll()
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
