package com.ecommerce.studentmarket.student.user.dtos;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.user.enums.GioiTinh;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
public class StudentResponseDto {

    private String mssv;

    private String hoTen;

    private String lop;

    private Integer khoa;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    private Date ngaySinh;

    private String sdt;

    private String role;

    private ImageDomain image;

    private Long idGioHang;

    private Long maGHDT;

}
