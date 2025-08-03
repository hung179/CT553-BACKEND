package com.ecommerce.studentmarket.student.store.dtos;


import com.ecommerce.studentmarket.product.item.dtos.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDtoResponse {

    private Long maGHDT;

    private String msh;

    private String moTa;

    private String hoTenSH;

    private String sdt;

    private Page<ProductResponseDto> products;
}
