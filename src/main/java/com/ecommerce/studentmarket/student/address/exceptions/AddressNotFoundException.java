package com.ecommerce.studentmarket.student.address.exceptions;

import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;

public class AddressNotFoundException extends AddressException {
    public AddressNotFoundException(Long maDC) {
        super("Không thể tìm thấy địa chỉ với mã  " + maDC);
    }
}
