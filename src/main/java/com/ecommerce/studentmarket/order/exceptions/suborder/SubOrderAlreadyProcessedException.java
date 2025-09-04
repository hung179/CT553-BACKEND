package com.ecommerce.studentmarket.order.exceptions.suborder;

import com.ecommerce.studentmarket.order.exceptions.order.OrderException;

public class SubOrderAlreadyProcessedException extends OrderException {
    public SubOrderAlreadyProcessedException(Long maDHC){
        super("Đơn hàng " + maDHC + " đã được xử lý, không thể hủy." );
    }
}
