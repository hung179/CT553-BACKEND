package com.ecommerce.studentmarket.product.item.exceptions;

public class ProductOutOfStockException extends ProductException {
    public ProductOutOfStockException(String tenSP, Long soLuong) {
        super("Sản phẩm "+ tenSP + " không đủ số lượng trong kho để thêm. Chỉ còn lại "+ soLuong + " trong kho.");
    }
}
