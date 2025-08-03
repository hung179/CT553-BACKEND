package com.ecommerce.studentmarket.product.items.exceptions;

public class ProductAlreadyDeletedException extends ProductException{
    public ProductAlreadyDeletedException(Long maSP){
        super("Sản phẩm " + maSP + " đã bị xóa");
    }
}
