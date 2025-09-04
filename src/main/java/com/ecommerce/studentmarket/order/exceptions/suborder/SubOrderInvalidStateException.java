package com.ecommerce.studentmarket.order.exceptions.suborder;

import com.ecommerce.studentmarket.order.exceptions.order.OrderException;

public class SubOrderInvalidStateException extends OrderException {
    public SubOrderInvalidStateException( String trangThai){
        super("Trạng thái" + trangThai +  "không hợp lệ !" );
    }
}
