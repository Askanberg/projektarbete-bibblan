package org.bibblan.core.util;

import org.bibblan.usermanagement.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;

    @Autowired
    public SecurityConfig(CustomOidcUserService customOidcUserService){
        this.customOidcUserService = customOidcUserService;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        return http.getSharedObject(AuthenticationManager.class);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/terms-and-conditions/**").permitAll()
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/login", "/oauth2/**", "/code/**" ,"/**").permitAll()
                        .requestMatchers("/login","/register").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/users/profile", "/css/**").permitAll()
                        .requestMatchers("/profile").permitAll()
                        .requestMatchers("/", "/terms-and-conditions").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login.html").permitAll()
                        .defaultSuccessUrl("/profile.html", true).permitAll()

                        .userInfoEndpoint(userInfo-> userInfo
                                .oidcUserService(customOidcUserService)))
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/profile.html", true)
                )
                .logout(logoutAction -> logoutAction
                        .logoutSuccessUrl("/home.html")
                        .permitAll());

        return http.build();
    }

}