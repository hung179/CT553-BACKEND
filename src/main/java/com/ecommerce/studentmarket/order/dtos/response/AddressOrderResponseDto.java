package com.ecommerce.studentmarket.order.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressOrderResponseDto {
    private Long maDCDH;

    private String tinhDCDH;

    private String huyenDCDH;

    private String xaDCDH;

    private String chiTietDCDH;
}
