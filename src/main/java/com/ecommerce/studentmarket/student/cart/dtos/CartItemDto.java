package com.ecommerce.studentmarket.student.cart.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDto {

    @NotNull(message = "Mã sản phẩm trong chi tiết giỏ hàng không được để trống")
    private Long maSP;

    @Min(value = 0, message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer soLuong;

}
