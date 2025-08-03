package com.ecommerce.studentmarket.student.ewallet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EwalletResponseDto {

    private Long maVDT;

    private BigDecimal soDuVDT;

    private Page<TransactionResponseDto> transactions;

}
