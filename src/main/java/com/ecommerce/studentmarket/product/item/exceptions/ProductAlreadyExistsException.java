package com.ecommerce.studentmarket.product.items.exceptions;

public class ProductAlreadyExistsException extends ProductException{
    public ProductAlreadyExistsException(Long maSP) {
        super("Sản phẩm với mã " + maSP + " đã tồn tại");
    }
}
