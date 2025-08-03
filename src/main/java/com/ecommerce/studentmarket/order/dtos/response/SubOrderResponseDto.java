package com.ecommerce.studentmarket.order.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubOrderResponseDto {

    private Long maDHC;

    private Long maGianHangDHC;

    private OrderStateResponseDto orderState;

    private List<OrderItemResponseDto> orderItems;
}
