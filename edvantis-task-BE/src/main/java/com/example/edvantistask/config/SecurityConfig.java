package com.example.edvantistask.config;

import com.example.edvantistask.model.UserAccount;
import com.example.edvantistask.repository.UserAccountRepository;
import com.example.edvantistask.service.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/emergency-calls/**").hasAnyRole("READ", "WRITE", "USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/emergency-calls/**").hasAnyRole("WRITE", "USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/emergency-calls/**").hasAnyRole("WRITE", "USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/emergency-calls/**").hasAnyRole("WRITE", "USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CommandLineRunner initDefaultAdmin(UserAccountRepository userAccountRepository,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            if (userAccountRepository.findByUsername("admin").isEmpty()) {
                UserAccount admin = UserAccount.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role("ROLE_ADMIN")
                        .build();
                userAccountRepository.save(admin);
            }
        };
    }
}
