package com.a2sv.bankdashboard.config;

//import com.a2sv.bankdashboard.dto.response.AuthenticationResponse;
//import com.a2sv.bankdashboard.exception.ResourceNotFoundException;
import com.a2sv.bankdashboard.filter.JwtAuthenticationFilter;
import com.a2sv.bankdashboard.service.AuthenticationService;
import com.a2sv.bankdashboard.service.UserDetailsServiceImp;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomLogoutHandler logoutHandler;



    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomLogoutHandler logoutHandler) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req.requestMatchers("/login/**","/register/**", "/refresh_token/**","/auth/**", "/swagger-ui/**",  "/v3/api-docs",  "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/admin_only/**").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
//                ).oauth2Login(oauth2 -> oauth2
//                        .successHandler(this::oauth2SuccessHandler)
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .oidcUserService(oidcUserService())
//                        )
                ).userDetailsService(userDetailsServiceImp)
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e->e.accessDeniedHandler(
                                        (request, response, accessDeniedException)->response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(l->l
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
                        ))
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

//    private void oauth2SuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
//
//        try {
//            AuthenticationResponse<Void> authResponse = authenticationService.authenticate(oidcUser);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
//            response.setStatus(HttpStatus.OK.value());
//        } catch (ResourceNotFoundException e) {
//            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
//        }
//
//    }



}