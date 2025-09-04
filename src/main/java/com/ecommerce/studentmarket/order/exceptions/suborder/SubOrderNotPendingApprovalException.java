package com.ecommerce.studentmarket.order.exceptions.suborder;

import com.ecommerce.studentmarket.order.exceptions.order.OrderException;

public class SubOrderNotPendingApprovalException extends OrderException {
    public SubOrderNotPendingApprovalException(Long maDHC){
        super("Không tìm thấy trạng thái đơn hàng với mã đơn hàng con:  " + maDHC);
    }
}
