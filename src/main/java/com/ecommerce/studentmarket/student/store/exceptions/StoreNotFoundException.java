package com.ecommerce.studentmarket.student.store.exceptions;


public class StoreNotFoundException extends StoreException {
    public StoreNotFoundException(String msh) {
        super("Không tìm thấy gian hàng với mã số sỡ hữu"+ msh);
    }
}