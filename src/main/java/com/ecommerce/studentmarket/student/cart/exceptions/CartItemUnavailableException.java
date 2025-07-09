package com.ecommerce.studentmarket.student.cart.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class CartItemUnavailableException extends CartException{
    public CartItemUnavailableException(Long cartItemId){
        super("Sản phẩm " + cartItemId + " không còn khả dụng.");
    }
}
