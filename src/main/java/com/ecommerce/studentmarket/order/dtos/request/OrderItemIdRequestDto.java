package com.ecommerce.studentmarket.order.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemIdRequestDto {

    private Long maDH;

    private Long maSP;
}
