package com.ecommerce.studentmarket.student.store.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreDtoRequest {

    @Size(min = 50, message = "Mô tả gian hàng không được dưới ")
    private String moTa;
}
