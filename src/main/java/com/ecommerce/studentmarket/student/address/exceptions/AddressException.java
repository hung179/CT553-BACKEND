package com.ecommerce.studentmarket.student.address.exceptions;

public class AddressException extends RuntimeException {
    public AddressException(String message) {
        super(message);
    }

    public AddressException(String message, Throwable cause){ super(message, cause);}
}
