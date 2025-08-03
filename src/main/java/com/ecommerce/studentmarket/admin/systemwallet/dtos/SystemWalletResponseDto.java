package com.ecommerce.studentmarket.admin.systemwallet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemWalletResponseDto {

    private Long maVHT;

    private BigDecimal soDuVHT;

    private Page<SystemTransactionResponseDto> transactions;

}
