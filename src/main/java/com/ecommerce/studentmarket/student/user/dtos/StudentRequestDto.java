package com.ecommerce.studentmarket.student.user.dtos;

import com.ecommerce.studentmarket.common.cloudinary.dtos.ImageDto;
import com.ecommerce.studentmarket.student.user.enums.GioiTinh;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class StudentRequestDto {

    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã số chỉ được chứa chữ hoa và số")
    @Size(min = 5, max = 20, message = "Mã số phải từ 5-20 ký tự")
    private String mssv;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ tên chỉ được chứa chữ cái và khoảng trắng")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    private String hoTen;

    @Pattern(regexp = "^[A-Z0-9]+$", message = "Lớp chỉ được chứa chữ hoa và số")
    private String lop;

    @Min(value = 1, message = "Khóa chỉ từ 1 trở lên")
    private Integer khoa;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Mật khẩu phải có ít nhất 1 chữ thường, 1 chữ hoa, 1 số và 1 ký tự đặc biệt")
    @Size(min = 8, max = 20, message = "Mật khẩu phải từ 8-20 ký tự")
    private String password;

    private GioiTinh gioiTinh;

    private TrangThai trangThai;

    private ImageDto image;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private Date ngaySinh;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    @Size(min = 10, max = 11, message = "Số điện thoại phải từ 10-11 số")
    private String sdt;
}
