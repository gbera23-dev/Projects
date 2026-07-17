package com.epam.finaltask.service;

import com.epam.finaltask.model.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user with username {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );
        if(authentication.isAuthenticated()) {
            log.info("successful! user with name {} was authenticated", loginRequest.getUsername());
            return jwtService.generateToken(loginRequest.getUsername());
        }
        log.warn("Failed to authenticate user with name {}", loginRequest.getUsername());
        return "Failure";
    }
}
