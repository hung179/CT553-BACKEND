package com.ecommerce.studentmarket.student.cart.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(CartItemIdDomain cartItemId) {
        super("Sản phẩm " + cartItemId.getIdSP() + " không thể tìm thấy trong giỏ hàng");
    }
}
