package com.ecommerce.studentmarket.student.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThai {
    HOATDONG("Hoạt động"),
    DINHCHI("Đình chỉ");

    private final String displayName;

    TrangThai(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static TrangThai fromString(String value) {
        for (TrangThai tt : TrangThai.values()) {
            if (tt.displayName.equals(value)) {
                return tt;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + value);
    }
}
