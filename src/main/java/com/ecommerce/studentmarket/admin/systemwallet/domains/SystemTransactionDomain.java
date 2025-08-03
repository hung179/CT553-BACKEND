package com.ecommerce.studentmarket.admin.systemwallet.domains;

import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GIAO_DICH_HE_THONG")
public class SystemTransactionDomain {

    @Id
    @NotNull(message = "Mã giao dịch không được để trống")
    private Long maGDHT;

    @NotNull(message = "Số tiền giao dịch không được để trống")
    private BigDecimal soTienGDHT;

    private LoaiGiaoDich loaiGDHT;

    @NotNull(message = "Thời gian giao dịch không được để trống")
    private LocalDateTime thoiGianGDHT;

    private TrangThaiGiaoDich trangThaiGDHT;

    @Column(unique = true)
    private String idGiaoDichHT;

    private Long idDonHangGDHT;

    @NotBlank(message = "Chi tiết giao dịch không được để trống")
    private String chiTietGDHT;

    @NotBlank(message = "Mã số sinh viên giao dịch không được để trống")
    private String mssvGDHT;

    @NotBlank(message = "Họ tên sinh viên giao dịch không được để trống")
    private String hoTenSVGDHT;

    @ManyToOne
    @JoinColumn(name = "maVHT", referencedColumnName = "maVHT")
    @NotNull(message = "Ví hệ thống trong giao dịch hệ thống không được để trống")
    @JsonIgnore
    private SystemWalletDomain systemWallet;

}
