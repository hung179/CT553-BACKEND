package com.ecommerce.studentmarket.student.cart.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemIdDto {
    private Long idGioHang;
    private Long idSP;
}
