package com.ecommerce.studentmarket.student.ewallet.domains;

import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GIAO_DICH")
public class TransactionDomain {

    @Id
    @NotNull(message = "Mã giao dịch không được để trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionSeq")
    @SequenceGenerator(
            name = "transactionSeq",
            sequenceName = "transactionIdSeq",
            allocationSize = 1000000
    )
    private Long maGD;

    @NotNull(message = "Số tiền giao dịch không được để trống")
    private BigDecimal soTienGD;

    private LoaiGiaoDich loaiGD;

    @NotNull(message = "Thời gian giao dịch không được để trống")
    private LocalDateTime thoiGianGD;

    private TrangThaiGiaoDich trangThaiGD;

    @Column(unique = true)
    private String idGiaoDich;

    private Long idDonHangGD;

    private String chiTietGD;

    @ManyToOne
    @JoinColumn(name = "maVDT", referencedColumnName = "maVDT")
    @NotNull(message = "Ví điện tử giao dịch không được để trống")
    @JsonIgnore
    private EwalletDomain wallet;

}
