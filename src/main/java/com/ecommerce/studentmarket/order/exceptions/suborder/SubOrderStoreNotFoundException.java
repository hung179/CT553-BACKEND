package com.ecommerce.studentmarket.order.exceptions.suborder;


import com.ecommerce.studentmarket.student.store.exceptions.StoreException;

public class SubOrderStoreNotFoundException extends StoreException {
    public SubOrderStoreNotFoundException(Long maGianHangDHC) {
        super("Không tìm thấy gian hàng với mã số "+ maGianHangDHC);
    }
}