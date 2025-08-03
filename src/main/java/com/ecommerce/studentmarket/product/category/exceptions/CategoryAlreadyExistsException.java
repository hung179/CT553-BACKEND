package com.ecommerce.studentmarket.product.category.exceptions;

import com.ecommerce.studentmarket.product.item.exceptions.ProductException;

public class CategoryAlreadyExistsException extends CategoryException {
    public CategoryAlreadyExistsException(String tenDM) {
        super("Danh mục với mã " + tenDM + " đã tồn tại");
    }
}
