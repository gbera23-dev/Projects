package com.epam.finaltask.service;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername() {
        String username = "username";
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(); grantedAuthorities.add(grantedAuthority);
        UserDTO userDTO = new UserDTO(); userDTO.setUsername(username);
        userDTO.setPassword("password"); userDTO.setRole("ROLE_ADMIN");
        User expectedUser = new User("username", "password", grantedAuthorities);
        when(userService.getUserByUsername(username)).thenReturn(userDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull("Returned userDetails must not be null", userDetails);
        assertEquals("usernames must match", userDetails.getUsername(), username);
        assertEquals("passwords must match", userDetails.getPassword(), "password");
        assertEquals("authorities must match", userDetails.getAuthorities().toString(), "[ROLE_ADMIN]");
        verify(userService, times(1)).getUserByUsername(username);
    }
}
