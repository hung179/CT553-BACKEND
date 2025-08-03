package com.ecommerce.studentmarket.student.cart.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class CartItemUnavailableException extends CartException{
    public CartItemUnavailableException(String tenSP){
        super("Sản phẩm " + tenSP + " không còn khả dụng.");
    }
}
