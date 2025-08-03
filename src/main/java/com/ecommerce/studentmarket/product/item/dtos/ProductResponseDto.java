package com.ecommerce.studentmarket.product.item.dtos;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.product.category.dtos.CategoryResponseDto;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class ProductResponseDto {

    private Long maSP;

    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Tên sản phẩm chỉ được chứa chữ cái và khoảng trắng")
    @Size(min = 2, max = 100, message = "Tên sản phẩm phải từ 2-100 ký tự")
    private String tenSP;

    @Min(value = 1, message = "Mã sở hữu gian hàng phải lớn hơn 0")
    private Long maGHSH;

    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn 0")
    @Max(value = 999999999, message = "Giá sản phẩm không được vượt quá 999,999,999")
    @Digits(integer = 9, fraction = 0, message = "Giá sản phẩm không hợp lệ")
    private Long giaSP;

    @Min(value = 0, message = "Số lượng không được âm")
    @Max(value = 99999, message = "Số lượng không được vượt quá 99,999")
    private Integer soLuong = 1;

    @Size(max = 50, message = "Kích thước không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[\\p{L}\\s\\d×x*-]+$", message = "Kích thước chỉ được chứa chữ, số, dấu × và -")
    private String kichThuoc;

    @Min(value = 0, message = "Trọng lượng không được âm")
    @Max(value = 100000, message = "Trọng lượng không được vượt quá 100,000 gram")
    private Integer trongLuong;

    @Size(min = 50, message = "Mô tả sản phẩm phải có ít nhất 50 ký tự")
    private String moTa;

    private Boolean daAn;

    private List<ImageDomain> images;

    private String mssv;

    private String hoTen;

    private CategoryResponseDto danhMuc;
}
