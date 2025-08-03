package com.ecommerce.studentmarket.student.cart.domains;

import com.ecommerce.studentmarket.product.item.dtos.ProductResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "CHI_TIET_GIO_HANG")
public class CartItemDomain {

    @EmbeddedId
    private CartItemIdDomain id;

    @ManyToOne
    @MapsId("idGioHang")
    @JoinColumn(name = "idGioHang")
    @NotNull(message = "Giỏ hàng trong chi tiết giỏ hàng không được null")
    @JsonIgnore
    private CartDomain cartDomain;

    @NotNull(message = "Sản phẩm trong chi tiết giỏ hàng không được null")
    private Long maSP;


    private Integer soLuong = 1;

    @Transient
    private ProductResponseDto sanPham;
}
