package com.epam.finaltask.exception;

import com.epam.finaltask.exception.authExceptions.UnableToAuthorizeUserException;
import com.epam.finaltask.exception.userExceptions.UserAlreadyExistsException;
import com.epam.finaltask.exception.userExceptions.UserCouldNotBeSavedException;
import com.epam.finaltask.exception.userExceptions.UserInsufficientFundsException;
import com.epam.finaltask.exception.userExceptions.UserNotFoundException;
import com.epam.finaltask.exception.voucherExceptions.VoucherAlreadyPurchasedException;
import com.epam.finaltask.exception.voucherExceptions.VoucherCancelledException;
import com.epam.finaltask.exception.voucherExceptions.VoucherCouldNotBeSavedException;
import com.epam.finaltask.exception.voucherExceptions.VoucherNotFoundException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnableToAuthorizeUserException.class)
    public ResponseEntity<String> handleUserAuthorization(UnableToAuthorizeUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistingUser(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNonExistingUser(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(VoucherNotFoundException.class)
    public ResponseEntity<String> handleNonExistingUser(VoucherNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(VoucherCouldNotBeSavedException.class)
    public ResponseEntity<String> handleCannotSaveVoucher(VoucherCouldNotBeSavedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(UserCouldNotBeSavedException.class)
    public ResponseEntity<String> handleCannotSaveUser(UserCouldNotBeSavedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(UserInsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(UserInsufficientFundsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(VoucherCancelledException.class)
    public ResponseEntity<String> handleCancelledVoucher(VoucherCancelledException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(VoucherAlreadyPurchasedException.class)
    public ResponseEntity<String> handleAlreadyPurchasedVoucher(VoucherAlreadyPurchasedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("validationMessage",e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        response.put("statusMessage", e.getStatusCode());
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedType(UnexpectedTypeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("validationMessage",e.getLocalizedMessage());
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralIssue(Exception e) {
        System.out.println("exception type is " + e.getClass());
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
