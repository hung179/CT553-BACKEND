package com.ecommerce.studentmarket.common.ApiConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Boolean success;
}
