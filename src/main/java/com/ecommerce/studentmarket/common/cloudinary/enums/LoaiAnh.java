package com.ecommerce.studentmarket.common.cloudinary.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoaiAnh {

    DAIDIEN("Đại diện"),
    MOTA("Mô tả");

    private String displayName;

    @JsonCreator
    public static LoaiAnh fromString(String value) {
        for (LoaiAnh la : LoaiAnh.values()) {
            if (la.displayName.equals(value)) {
                return la;
            }
        }
        throw new IllegalArgumentException("Loại ảnh không hợp lệ: " + value);
    }

}
