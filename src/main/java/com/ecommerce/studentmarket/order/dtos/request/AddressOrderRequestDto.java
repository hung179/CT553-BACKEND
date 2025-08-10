package com.ecommerce.studentmarket.order.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressOrderRequestDto {

    private String tinhDCDH;

    private String huyenDCDH;

    private String xaDCDH;

    private String chiTietDCDH;
}
