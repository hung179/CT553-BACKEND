package com.ecommerce.studentmarket.order.domains;

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
public class OrderItemIdDomain implements Serializable {

    @NotNull(message = "Mã đơn hàng trong chi tiết đơn hàng không được để trống")
    private Long maDH;

    @NotNull(message = "Mã của sản phẩm trong chi tiết đơn hàng không được để trống")
    private Long maSP;

}
