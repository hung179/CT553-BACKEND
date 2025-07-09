package com.ecommerce.studentmarket.common.cloudinary.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChuSoHuu {

    SANPHAM("Sản phẩm"),
    NGUOIDUNG("Người dùng"),
    KHIEUNAI("Khiếu nại");

    private String displayName;

    @JsonCreator
    public static ChuSoHuu fromString(String value) {
        for (ChuSoHuu csh : ChuSoHuu.values()) {
            if (csh.displayName.equals(value)) {
                return csh;
            }
        }
        throw new IllegalArgumentException("Chủ sở hữu không hợp lệ: " + value);
    }
}
