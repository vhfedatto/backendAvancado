package com.example.frankenstein.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Primeira mudança -> H2 Console usa frames -> precisei burlar o Spring Security, que bloqueia isso.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desliga a proteção CSRF - H2 costuma quebrar com a proteção ligada, principalmente em ambiente local.
            .headers(headers -> headers // Configura os headers HTTP de segurança.
                .frameOptions(frame -> frame.sameOrigin()) // Permite que a página seja carregada dentro de frame ou iframe quando a origem for a mesma. Sem isso, o navegador bloqueia a renderização
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Define as regras de autorização das URLs. Libera qualquer rota começando com /h2-console/
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("12345")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
}
