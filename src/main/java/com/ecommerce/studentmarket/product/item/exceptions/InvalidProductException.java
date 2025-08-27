package com.ecommerce.studentmarket.student.user.exceptions;

import com.ecommerce.studentmarket.product.item.exceptions.ProductException;

public class InvalidProductException extends ProductException {
    public InvalidProductException(String message) {
        super(message);
    }
}
