package com.ecommerce.studentmarket.student.cart.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class CartItemProductOutOfStockException extends RuntimeException {
    public CartItemProductOutOfStockException(String tenSP, Long soLuong) {
        super("Sản phẩm "+ tenSP + " không đủ số lượng trong kho để thêm. Chỉ còn lại "+ soLuong + " trong kho.");
    }
}
