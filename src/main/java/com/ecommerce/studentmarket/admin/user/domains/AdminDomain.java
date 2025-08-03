package com.ecommerce.studentmarket.admin.user.domains;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "maVHT", referencedColumnName = "maVHT")
    @NotNull(message = "Ví hệ thống liên kết với quản trị viên không được để trống")
    @JsonIgnore
    private SystemWalletDomain systemWallet;
}
