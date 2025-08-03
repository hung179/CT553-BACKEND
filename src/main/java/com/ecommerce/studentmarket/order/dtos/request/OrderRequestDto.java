package com.ecommerce.studentmarket.order.dtos.request;

import com.ecommerce.studentmarket.order.Enums.ThanhToan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String mssvDH;

    @Min(value = 1, message = "Tổng tiền đơn hàng phải lớn hơn 0")
    private BigDecimal tongTienDH;

    private ThanhToan thanhToan;

    @Valid
    private List<SubOrderRequestDto> subOrder;
}
