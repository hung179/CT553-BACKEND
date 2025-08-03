package com.ecommerce.studentmarket.admin.user.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminResponseDto {

    @NotEmpty(message = "MSCB không được để trống")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã số chỉ được chứa chữ hoa và số")
    @Size(min = 5, max = 20, message = "Mã số phải từ 5-20 ký tự")
    private String mscb;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên chỉ được chứa chữ cái và khoảng trắng")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    private String hoTen;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    @Size(min = 10, max = 11, message = "Số điện thoại phải từ 10-11 số")
    private String sdt;

//  Đủ thời gian thì chuyển cái này thành enum
    private String role;
}
