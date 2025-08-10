package com.ecommerce.studentmarket.order.dtos.response;

import com.ecommerce.studentmarket.order.Enums.ThanhToan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long maDH;

    private String mssvDH;

    private LocalDateTime ngayTaoDH;

    private BigDecimal tongTienDH;

    private ThanhToan thanhToan;

    private LocalDateTime ngayThanhToanDH;

    private List<SubOrderResponseDto> subOrder;

    private AddressOrderResponseDto addressResponseDto;
}
