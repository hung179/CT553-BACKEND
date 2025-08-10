package com.ecommerce.studentmarket.admin.systemwallet.dtos;

import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemTransactionRequestDto {

    private Long maGDHT;

    @Positive(message = "Số tiền giao dịch phải lớn hơn 0")
    private BigDecimal soTienGDHT;

    private LoaiGiaoDich loaiGDHT;

    @PastOrPresent(message = "Thời gian giao dịch không được lớn hơn thời gian hiện tại")
    private LocalDateTime thoiGianGDHT;

    private TrangThaiGiaoDich trangThaiGDHT;

    private Long idDonHangGDHT;

    @Size(max = 255, message = "Chi tiết giao dịch không được vượt quá 255 ký tự")
    private String chiTietGDHT;

    private String idGiaoDichHT;

    private String mssvGDHT;

    private String hoTenSVGDHT;
}
