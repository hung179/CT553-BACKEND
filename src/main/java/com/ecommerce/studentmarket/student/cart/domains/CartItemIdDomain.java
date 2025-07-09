package com.ecommerce.studentmarket.student.cart.domains;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CartItemIdDomain implements Serializable {

    @NotNull(message = "Id của giỏ hàng trong chi tiết giỏ hàng không được để trống")
    private Long idGioHang;

    @NotNull(message = "Id của sản phẩm trong chi tiết giỏ hàng không được để trống")
    private Long idSP;

}
