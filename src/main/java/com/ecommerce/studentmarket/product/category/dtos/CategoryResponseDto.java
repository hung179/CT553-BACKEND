package com.ecommerce.studentmarket.product.category.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long maDM;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Tên danh mục chỉ được chứa chữ cái và khoảng trắng")
    @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2-100 ký tự")
    private String tenDM;

}
