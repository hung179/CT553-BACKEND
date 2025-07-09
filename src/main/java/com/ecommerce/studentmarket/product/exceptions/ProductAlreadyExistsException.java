package com.ecommerce.studentmarket.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ProductAlreadyExistsException extends ProductException{
    public ProductAlreadyExistsException(Long maSP) {
        super("Sản phẩm với mã " + maSP + " đã tồn tại");
    }
}
