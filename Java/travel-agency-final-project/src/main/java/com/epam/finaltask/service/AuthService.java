package com.epam.finaltask.service;

import com.epam.finaltask.model.LoginRequest;

public interface AuthService {
    String authenticateUser(LoginRequest loginRequest);
}
