package com.ecommerce.studentmarket.student.cart.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class CartItemProductOutOfStockException extends RuntimeException {
    public CartItemProductOutOfStockException(Long ItemId, Integer soLuong) {
        super("Sản phẩm "+ ItemId + " không đủ số lượng trong kho để thêm. Chỉ còn lại "+ soLuong + " trong kho.");
    }
}
