package com.epam.finaltask.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    public String generateToken(String username);

    public String extractUsername(String jwtToken);

    Boolean validateToken(String jwtToken, UserDetails userDetails);
}
