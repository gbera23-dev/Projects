package com.epam.finaltask.exception.voucherExceptions;

public class VoucherNotFoundException extends RuntimeException {
    public VoucherNotFoundException(String message) {
        super(message);
    }
}
