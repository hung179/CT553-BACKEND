package com.ecommerce.studentmarket.common.authencation.exceptions;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException(String message) {
        super(message);
    }
}
