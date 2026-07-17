package com.epam.finaltask.service;

import com.epam.finaltask.exception.authExceptions.UnableToAuthorizeUserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class JWTServiceImpl implements JWTService{
    private String secretKey = "";
    public JWTServiceImpl() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        secretKey = Base64.getEncoder().encodeToString(
                keyGenerator.generateKey().getEncoded());
    }

    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        String generatedToken = null;
        try {
            generatedToken = Jwts.builder().addClaims(claims)
                    .setSubject(username).
                    setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                    .signWith(getKey())
                    .compact();
        } catch(Exception e) {
            log.error("Failed to generate a token for user with name {}", username);
            throw e;
        }
        log.info("Token was generated for user {}", username);
        return generatedToken;
    }

    @Override
    public String extractUsername(String jwtToken) {
        log.info("Extracting username from jwtToken");
        String extracted =  extractClaim(jwtToken, Claims::getSubject);
        if(extracted == null) {
            log.warn("JWT token contained null username");
            return null;
        }
        log.info("Username {} was successfully extracted from jwtToken", extracted);
        return extracted;
    }

    @Override
    public Boolean validateToken(String jwtToken, UserDetails userDetails) {
        log.info("Validating the JWT token");
        String userName = extractUsername(jwtToken);
        Boolean result =  (userName.equals(userDetails.getUsername())
        && !isTokenExpired(jwtToken));
        if(result) {
            log.info("JWT Token validation was successful!");
        } else {
            log.warn("Failed JWT Token validation");
        }
        return result;
    }

    private Boolean isTokenExpired(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch(Exception e) {
            log.error("Failed JWT Token validation");
            throw new UnableToAuthorizeUserException("JWT Token validation failed");
        }
        return claims;
    }


    private SecretKey getKey() {
        byte[] keyValue = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyValue);
    }

}
