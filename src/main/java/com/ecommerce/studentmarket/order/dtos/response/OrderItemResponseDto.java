package com.ecommerce.studentmarket.order.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {

    private OrderItemIdRequestDto orderItemId;

    private Long giaSP;



}
