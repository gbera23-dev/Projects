package com.oop.web_project.services;

import com.oop.web_project.entities.Customer;
import com.oop.web_project.entities.Role;
import com.oop.web_project.exceptions.customerExceptions.CustomerCannotBeAuthenticatedException;
import com.oop.web_project.exceptions.customerExceptions.CustomerIsNotActiveException;
import com.oop.web_project.exceptions.customerExceptions.CustomerNotFoundException;
import com.oop.web_project.persistence.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CustomerRepository customerRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JWTService jwtService, CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
    }

    @Override
    public String authenticateCustomer(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );

        if(!authentication.isAuthenticated()) {
            throw new CustomerCannotBeAuthenticatedException("Customer cannot be authenticated!");
        }

        GrantedAuthority grantedAuthority = authentication.getAuthorities().stream().findFirst()
                .orElseThrow(
                        () -> new CustomerCannotBeAuthenticatedException("Customer cannot be authenticated!")
                );

        Customer customer = customerRepository.getCustomerByEmail(email)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer cannot be found!")
                );

        if(!customer.isActive()) {
            throw new CustomerIsNotActiveException("Customer is not active!");
        }

        return jwtService.generateToken(email, Role.valueOf(grantedAuthority.getAuthority()));
    }

}
