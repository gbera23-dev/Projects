package com.epam.finaltask.exception.userExceptions;

public class UserInsufficientFundsException extends RuntimeException {
    public UserInsufficientFundsException(String message) {
        super(message);
    }
}
