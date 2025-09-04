package com.ecommerce.studentmarket.order.exceptions.order;

public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(Long maDH){
        super("Không tìm thấy đơn hàng với mã: " + maDH);
    }
}
