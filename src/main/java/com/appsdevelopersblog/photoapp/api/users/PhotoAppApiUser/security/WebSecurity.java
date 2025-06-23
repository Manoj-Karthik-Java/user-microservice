package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.security;


import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment environment;

    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.userService = userService;
    }
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();


        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authz -> authz
                .anyRequest().access(
                        new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")
                )
        );

        http.addFilter(new AuthenticationFilter(userService,environment,authenticationManager));
        http.authenticationManager(authenticationManager);
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        return http.build();
    }



}
/*
An error occurred when I am trying to hit the api from apigateway using postman
forbidden and internal server

It is resolved by moving up this property gateway.ip=127.0.0.1 in application.properties file from line 15 to line 7

In Spring Boot, @Value or Environment.getProperty(...) reads values from the application context as soon as the class is constructed.
If your gateway.ip property was too far down or misordered,
Spring might not have had it available when WebSecurity was being created — leading to it using null, which would cause:

hasIpAddress('null') → always fails → 403 Forbidden

 That confirms the issue was due to the gateway.ip property being declared too late in the application.properties file
 Spring couldn’t pick it up in time for the WebSecurity configuration.

✅ Why this worked:
Spring reads properties top-down when building the application context. By placing:
gateway.ip=127.0.0.1
at the top, it ensures the Environment can resolve it before the security filter chain is initialized.
 */