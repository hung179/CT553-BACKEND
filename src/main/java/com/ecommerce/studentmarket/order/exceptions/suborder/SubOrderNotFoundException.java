package com.ecommerce.studentmarket.order.exceptions.suborder;

import com.ecommerce.studentmarket.order.exceptions.order.OrderException;

public class SubOrderNotFoundException extends OrderException {
    public SubOrderNotFoundException(Long maDHC){
        super("Không tìm thấy đơn hàng con với mã: " + maDHC);
    }
}
