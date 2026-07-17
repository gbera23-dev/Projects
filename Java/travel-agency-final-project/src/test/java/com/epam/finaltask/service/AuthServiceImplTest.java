package com.epam.finaltask.service;

import com.epam.finaltask.model.LoginRequest;
import com.epam.finaltask.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private  AuthenticationManager authenticationManager;

    @Mock
    private  JWTService jwtService;

    @Mock Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testAuthenticateUser() {
        String expectedJWTToken = "JWT token";
        LoginRequest loginRequest = new LoginRequest("username", "password");
        when(authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(loginRequest.getUsername()))
                .thenReturn("JWT token");
        String result = authService.authenticateUser(loginRequest);
        assertNotNull(result, "The resulting JWT token should not be null");
        assertEquals("resulting JWT token must match the expected JWT token", result, expectedJWTToken);
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        verify(authentication, times(1)).isAuthenticated();
        verify(jwtService, times(1)).generateToken(loginRequest.getUsername());
    }
}
