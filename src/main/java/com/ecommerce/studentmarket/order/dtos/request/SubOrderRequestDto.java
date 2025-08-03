package com.ecommerce.studentmarket.order.dtos.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubOrderRequestDto {

    private Long maGianHangDHC;

    @Valid
    private List<OrderItemRequestDto> orderItem;

}
