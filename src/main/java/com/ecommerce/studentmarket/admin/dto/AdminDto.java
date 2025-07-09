package com.ecommerce.studentmarket.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminDto {

    @NotEmpty(message = "MSCB không được để trống")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã số chỉ được chứa chữ hoa và số")
    @Size(min = 5, max = 20, message = "Mã số phải từ 5-20 ký tự")
    private String mscb;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Mật khẩu phải có ít nhất 1 chữ thường, 1 chữ hoa, 1 số và 1 ký tự đặc biệt")
    @Size(min = 8, max = 20, message = "Mật khẩu phải từ 8-20 ký tự")
    private String password;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên chỉ được chứa chữ cái và khoảng trắng")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    private String hoTen;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    @Size(min = 10, max = 11, message = "Số điện thoại phải từ 10-11 số")
    private String sdt;
}
