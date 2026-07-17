package com.epam.finaltask.service;


import com.epam.finaltask.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class JWTServiceImplTest {


    private JWTService jwtService = new JWTServiceImpl();

    public JWTServiceImplTest() throws NoSuchAlgorithmException {}

    @Test
    void testGenerateToken() {
        String username = "username";
        String generatedToken = jwtService.generateToken(username);
        int periodCount = count(generatedToken, '.');
        int blankCount = count(generatedToken, ' ');
        assertNotNull("generated token should not be null", generatedToken);
        assertEquals("JWT token does not have correct amount of periods",2, periodCount);
        assertEquals("JWT token does not have correct amount of blank characters",0, blankCount);
    }

    @Test
    void testExtractUsername() {
        String JWTToken = jwtService.generateToken("username");
        String username = jwtService.extractUsername(JWTToken);
        assertNotNull("extracted username must not be null", username);
        assertEquals("", "username", username);
    }

    @Test
    void testValidateToken() {
        String jwtToken = jwtService.generateToken("username");
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(); grantedAuthorities.add(grantedAuthority);
        UserDetails userDetails = new User("username", "password", grantedAuthorities);
        Boolean result = jwtService.validateToken(jwtToken, userDetails);
        assertNotNull("validation result should not be null", result);
        assertEquals("validation result must match expected result", result, true);
    }

    int count(String generatedToken, char ch){
        int count = 0;
        for(int i = 0; i < generatedToken.length(); i++) {
            if(generatedToken.charAt(i) == ch)count++;
        }
        return count;
    }
}
