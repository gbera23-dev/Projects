package com.oop.web_project.utils;

import com.oop.web_project.exceptions.customerExceptions.CustomerDetailsNotFoundException;
import com.oop.web_project.exceptions.customerExceptions.CustomerIsNotAuthenticatedException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomerSecurityUtils {

    public static @NonNull String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new CustomerIsNotAuthenticatedException("Customer is not authenticated!");
        }

        UserDetails userDetails = (UserDetails)auth.getPrincipal();

        if(userDetails == null) {
            throw new CustomerDetailsNotFoundException("Could not find user details!");
        }

        return userDetails.getUsername();
    }
}
