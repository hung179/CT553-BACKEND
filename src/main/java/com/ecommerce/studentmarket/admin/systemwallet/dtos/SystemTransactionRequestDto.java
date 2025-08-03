package com.ecommerce.studentmarket.student.ewallet.dtos;

import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @Positive(message = "Số tiền giao dịch phải lớn hơn 0")
    private BigDecimal soTienGD;

    private LoaiGiaoDich loaiGD;

    @PastOrPresent(message = "Thời gian giao dịch không được lớn hơn thời gian hiện tại")
    private LocalDateTime thoiGianGD;

    private TrangThaiGiaoDich trangThaiGD;

    private Long idDonHangGD;

    @Size(max = 255, message = "Chi tiết giao dịch không được vượt quá 255 ký tự")
    private String chiTietGD;

}
