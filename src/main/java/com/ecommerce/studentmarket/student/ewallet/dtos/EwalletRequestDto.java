package com.ecommerce.studentmarket.student.ewallet.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EwalletRequestDto {

    @Min(value = 0, message = "Số dư ví điện tử chỉ từ 1 trở lên")
    private Long soDuVDT;

}
