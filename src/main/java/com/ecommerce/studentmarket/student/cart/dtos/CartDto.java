package com.ecommerce.studentmarket.student.cart.dtos;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto {

    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã số chỉ được chứa chữ hoa và số")
    @Size(min = 5, max = 20, message = "Mã số phải từ 5-20 ký tự")
    private String mssv;

    private List<CartItemDto> items = new ArrayList<>();

}
