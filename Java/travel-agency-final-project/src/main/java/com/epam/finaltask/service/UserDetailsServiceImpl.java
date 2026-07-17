package com.epam.finaltask.service;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.exception.userExceptions.UserNotFoundException;
import com.epam.finaltask.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        log.info("Loading user with username {}", username);
        UserDTO userDTO = userService.getUserByUsername(username);
        if(userDTO == null){
            log.error("Could not find user with username {}", username);
            throw new UserNotFoundException("User not found with username " + username);
        }
        log.info("User with name {} was successfully loaded!", username);
        return new User(userDTO.getUsername(), userDTO.getPassword(),
                getGrantedAuthorities(List.of(Role.valueOf(userDTO.getRole()))));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }
        System.out.print("Granted authorities are ");
        for(GrantedAuthority grantedAuthority : grantedAuthorities) {
            System.out.print(grantedAuthority.getAuthority()+" ");
        }System.out.println();
        return grantedAuthorities;
    }
}
