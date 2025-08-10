package com.ecommerce.studentmarket.order.dtos.response;


import com.ecommerce.studentmarket.order.domains.OrderItemIdDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private OrderItemIdResponseDto maCTDH;

    private OrderItemIdResponseDto orderItemId;

    private BigDecimal giaSP;

    private Long soLuong;
}
