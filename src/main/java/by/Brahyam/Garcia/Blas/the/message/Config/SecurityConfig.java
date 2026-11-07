package by.Brahyam.Garcia.Blas.the.message.Config;

import by.Brahyam.Garcia.Blas.the.message.Security.JwtAutenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAutenticationFilter jwtAutenticationFilter;
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/bra/auth/login",
            "/api/bra/auth/signup",
            "/api/bra/auth/validate-email",
            "/api/bra/auth/verify-email",
            "/api/bra/auth/resend-verification",
            "/api/bra/auth/forgot-password",
            "/api/bra/auth/reset-password",
            // ADD THIS LINE: Allow WebSocket handshake requests without prior authentication
            "/ws/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http.csrf(csrf->csrf.disable())
                .cors(cors->{})// Note: Fixed lowercase variable name convention here
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAutenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}
