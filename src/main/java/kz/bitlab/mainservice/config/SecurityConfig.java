package kz.bitlab.mainservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/sign-in", "/user/create", "/user/change-password").permitAll()
                        .requestMatchers("/api/course/**").hasRole("ADMIN")
                        .requestMatchers("/lesson/**").hasRole("ADMIN")// <-- добавлено
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/teacher/**").hasRole("TEACHER")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "TEACHER")
                        .requestMatchers("/file/upload").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/file/download/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakRoleConverter()))
                );

        return http.build();
    }

    @Bean
    public KeycloakRoleConverter keycloakRoleConverter() {
        return new KeycloakRoleConverter();
    }
}
