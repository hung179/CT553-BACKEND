package com.ecommerce.studentmarket.common.cloudinary.dtos;

import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ImageDto {

    @NotNull(message = "Id chủ sỡ hữu ở dto không được để trống")
    @Min(value = 1, message = "Mã chủ sở hữu phải lớn hơn 0")
    private Long idChuSoHuu;

    @NotNull(message = "Loại ảnh ở dto không được để trống")
    private LoaiAnh loaiAnh;

    @NotNull(message = "Chủ sỡ hữu ở dto không được để trống")
    private ChuSoHuu chuSoHuu;

}
