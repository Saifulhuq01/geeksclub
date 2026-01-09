package dev.sivalabs.geeksclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig {
    private static final String[] PUBLIC_RESOURCES = {
        "/", "/favicon.ico", "/actuator/**", "/error", "/swagger-ui/**", "/v3/api-docs/**",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.securityMatcher("/api/**");
        http.csrf(
                csrf -> csrf.ignoringRequestMatchers("/api/**") // Only if API is truly stateless with JWT
                );

        http.authorizeHttpRequests(r -> r.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers("/api/auth/login", "/api/auth/refresh")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/*")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/messages")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/messages/*")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()));
        http.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
