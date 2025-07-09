package com.ecommerce.studentmarket.student.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GioiTinh {
    NAM("Nam"),
    NU("Nữ"),
    KHAC("Khác");

    private final String displayName;

    GioiTinh(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static GioiTinh fromString(String value) {
        for (GioiTinh gt : GioiTinh.values()) {
            if (gt.displayName.equals(value)) {
                return gt;
            }
        }
        throw new IllegalArgumentException("Giới tính không hợp lệ: " + value);
    }
}
