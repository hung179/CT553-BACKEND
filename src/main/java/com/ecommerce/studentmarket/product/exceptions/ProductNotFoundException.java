package com.ecommerce.studentmarket.product.exceptions;

public class ProductNotFoundException extends ProductException{
    public ProductNotFoundException(Long maSP){
        super("Không tìm thấy sản phẩm với mã: " + maSP);
    }
}
