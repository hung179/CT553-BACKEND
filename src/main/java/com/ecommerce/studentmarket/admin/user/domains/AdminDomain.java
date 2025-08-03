package com.ecommerce.studentmarket.admin.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QUAN_TRI_VIEN")
public class AdminDomain {
    @Id
    @Column(unique = true)
    @NotEmpty(message = "MSCB không được để trống")
    private String mscb;

    @NotEmpty(message = "Mật khẩu cán bộ không được để trống")
    private String password;

    @NotEmpty(message = "Họ tên cán bộ không được để trống")
    private String hoTen;

    @Column(unique = true)
    @NotEmpty(message = "Sđt cán bộ không được để trống")
    private String sdt;

    @NotEmpty(message = "Role không được trống")
    private String role = "admin";
}
