package com.ecommerce.studentmarket.student.user.domains;


import com.ecommerce.studentmarket.student.address.domains.AddressDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.user.enums.GioiTinh;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

import static com.ecommerce.studentmarket.student.user.enums.TrangThai.HOATDONG;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SINH_VIEN")
public class StudentDomain {
    @Id
    @NotEmpty(message = "MSSV không được để trống")
    @Column(unique = true)
    private String mssv;

    @NotEmpty(message = "Họ tên sinh viên không được để trống")
    private String hoTen;

    @NotEmpty(message = "Lớp sinh viên không được để trống")
    private String lop;

    @NotNull(message = "Khóa sinh viên không được để trống")
    private Integer khoa;

    @NotEmpty(message = "Mật khẩu sinh viên không được để trống")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Giới tính sinh viên không được để trống")
    private GioiTinh gioiTinh;

    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = HOATDONG;

    @NotNull(message = "Ngày sinh sinh viên không được để trống")
    private Date ngaySinh;

    @NotEmpty(message = "Số điện thoại sinh viên không được để trống")
    @Column(unique = true)
    private String sdt;

    private Long imageId;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private CartDomain cart;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreDomain store;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private EwalletDomain wallet;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressDomain> addresses;

    @NotEmpty(message = "Role không được để trống")
    private String role = "student";
}
