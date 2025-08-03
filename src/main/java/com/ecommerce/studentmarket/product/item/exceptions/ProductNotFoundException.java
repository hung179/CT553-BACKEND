package com.ecommerce.studentmarket.product.item.exceptions;

public class ProductNotFoundException extends ProductException{
    public ProductNotFoundException(Long maSP){
        super("Không tìm thấy sản phẩm với mã: " + maSP);
    }
}
