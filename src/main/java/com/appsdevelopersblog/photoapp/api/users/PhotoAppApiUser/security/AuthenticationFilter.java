package com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.model.request.LoginRequestModel;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.service.UserService;
import com.appsdevelopersblog.photoapp.api.users.PhotoAppApiUser.shared.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService usersService;
    private Environment environment;

    public AuthenticationFilter(UserService usersService, Environment environment,
                                AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

	/*
		1. After Spring Security authenticates the user, it stores the authenticated user details in the Authentication object.
		2. auth.getPrincipal() returns an object, and we need to typecast it with User(from org.springframework.security.core.userdetails.User).
		3. We extract the username from it — usually the email address of the user in this case.
	*/

        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = usersService.getUserDetailsByEmail(userName);
        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes()); // Encodes the secret key using Base64 (JWT libraries expect it that way).
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes); // Uses io.jsonwebtoken.security.Keys to generate a secure signing key from the encoded secret.

        Instant now = Instant.now();


		/*
			In the below code .subject() is used to set the "sub" (subject) claim in the JWT payload.
			This is one of the standard registered claim names in the JWT specification.
			example JSON that describes the above scenario
			{
			  "sub": "a1234567-89bc-def0-1234-56789abcdef0",
			  "iat": 1718900000,
			  "exp": 1718986400
			}
		*/

		String token = Jwts.builder()
				.subject(userDetails.getUserId()) // Set the JWT subject to user's unique ID
				.expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time"))))) // Expiry
				.issuedAt(Date.from(now)) // Issued-at time
				.signWith(secretKey) // Sign the JWT with the secret key
				.compact(); // Build and serialize it

        res.addHeader("token", token);
        res.addHeader("userId", userDetails.getUserId());
    }
}


