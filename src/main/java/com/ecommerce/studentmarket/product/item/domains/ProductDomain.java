package com.ecommerce.studentmarket.product.item.domains;


import com.ecommerce.studentmarket.product.category.domains.CategoryDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SAN_PHAM")
public class ProductDomain {
    @Id
    @NotNull(message = "Id sản phẩm không được để trống")
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSeq")
    @SequenceGenerator(
            name = "productSeq",
            sequenceName = "productIdSeq",
            allocationSize = 1000000
    )
    private Long maSP;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSP;

    @NotNull(message = "Mã gian hàng sỡ hữu sản phẩm không được để trống")
    @Column(name = "maGHSH", columnDefinition = "BIGINT")
    private Long maGHSH;

    @NotNull(message = "Giá sản phẩm không được để trống")
    private Long giaSP;

    private Long soLuong = 1L;

    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    private String moTa;

    @NotBlank(message = "Kích thước sản phẩm không được để trống")
    private String kichThuoc;

    @NotNull(message = "Trọng lượng sản phẩm không được để trống")
    private Integer trongLuong;

    private Boolean daAn = false;

    private Boolean daXoa = false;

    private List<Long> imageIds;

    @ManyToOne
    @JoinColumn(name = "maDM", referencedColumnName = "maDM")
    @JsonIgnore
    private CategoryDomain category;
}
