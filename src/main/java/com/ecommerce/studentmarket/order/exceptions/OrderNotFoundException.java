package com.ecommerce.studentmarket.order.exceptions;

import com.ecommerce.studentmarket.product.category.exceptions.CategoryException;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(Long maDH){
        super("Không tìm thấy đơn hàng với mã: " + maDH);
    }
}
