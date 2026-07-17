package com.epam.finaltask.exception.voucherExceptions;

public class VoucherAlreadyPurchasedException extends RuntimeException {
    public VoucherAlreadyPurchasedException(String message) {
        super(message);
    }
}
