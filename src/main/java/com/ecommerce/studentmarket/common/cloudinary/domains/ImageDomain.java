package com.ecommerce.studentmarket.common.cloudinary.domains;


import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ANH")
public class ImageDomain {
    @Id
    @NotNull(message = "Id ảnh không được để trống")
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imageSeq")
    @SequenceGenerator(
            name = "imageSeq",
            sequenceName = "imageIdSeq",
            allocationSize = 1
    )
    private Long idImg;

    @NotBlank(message = "PublicId của ảnh không được để trống")
    private String publicId;

    @NotBlank(message = "Url của ảnh không được để trống")
    private String url;

    @NotNull(message = "Thứ tự ảnh không được để trống")
    private Integer thuTu;

    private Boolean daXoa = false;

    @NotNull(message = "Id chủ sỡ hữu ảnh không được để trống")
    private Long idChuSoHuu;

    @NotNull(message = "Loại ảnh không được để trống")
    @Enumerated(EnumType.STRING)
    private LoaiAnh loaiAnh;

    @NotNull(message = "Chủ sở hữu không được để trống")
    @Enumerated(EnumType.STRING)
    private ChuSoHuu chuSoHuu;
}
