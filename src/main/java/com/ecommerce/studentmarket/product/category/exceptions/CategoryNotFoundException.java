package com.ecommerce.studentmarket.product.category.exceptions;

import com.ecommerce.studentmarket.product.item.exceptions.ProductException;

public class CategoryNotFoundException extends CategoryException {
    public CategoryNotFoundException(Long maDM){
        super("Không tìm danh mục với mã: " + maDM);
    }
}
