package com.ecommerce.studentmarket.student.address.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private Long maDC;

    private String tinhDC;

    private String huyenDC;

    private String xaDC;

    private String chiTietDC;

    private Boolean macDinhDC;
}
