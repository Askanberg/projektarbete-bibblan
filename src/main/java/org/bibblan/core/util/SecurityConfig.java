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
                        .requestMatchers("/terms-and-conditions/**", "/home", "/login", "/register",
                                "/oauth2/**", "/css/**", "/api/users/**", "/api/users/profile", "/api/users/**").permitAll()
                        .requestMatchers("/profile").authenticated()
                        .anyRequest().authenticated()
                )
                .authorizeHttpRequests(adminAuth -> adminAuth
                        .requestMatchers("/admin/delete/**").hasRole("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/profile", true).permitAll()

                        .userInfoEndpoint(userInfo-> userInfo
                                .oidcUserService(customOidcUserService)))
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/profile", true)
                )
                .logout(logoutAction -> logoutAction
                        .logoutSuccessUrl("/home")
                        .permitAll());

        return http.build();
    }

}