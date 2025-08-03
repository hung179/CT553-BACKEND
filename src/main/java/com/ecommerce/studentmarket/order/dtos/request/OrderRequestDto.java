package com.ecommerce.studentmarket.order.dtos;

import com.ecommerce.studentmarket.order.Enums.ThanhToan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String mssvDH;

    private Long tongTienDH;

    private ThanhToan thanhToan;

    private List<SubOrderRequestDto> subOrder;
}
