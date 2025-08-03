package com.ecommerce.studentmarket.admin.systemwallet.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemWalletRequestDto {

    @Min(value = 0, message = "Số dư ví điện tử chỉ từ 1 trở lên")
    private Long soDuVHT;

}
