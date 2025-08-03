package com.ecommerce.studentmarket.order.dtos.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {

    @Valid
    private OrderItemIdRequestDto orderItemId;

    @Min(value = 1, message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal giaSP;

}
