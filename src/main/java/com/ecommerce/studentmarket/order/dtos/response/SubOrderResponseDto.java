package com.ecommerce.studentmarket.order.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubOrderRequestDto {

    private Long maGianHangDHC;

    private List<OrderItemRequestDto> orderItem;

}
