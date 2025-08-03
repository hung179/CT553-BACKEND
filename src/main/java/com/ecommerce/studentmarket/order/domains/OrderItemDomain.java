package com.ecommerce.studentmarket.order.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHI_TIET_DON_HANG")
public class OrderItemDomain {

    @EmbeddedId
    private OrderItemIdDomain maCTDH;

    @NotNull(message = "Giá sản phẩm không được để trống")
    private BigDecimal giaSP;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    private Long soLuong;

    @ManyToOne
    @JoinColumn(name = "maDHC")
    @NotNull(message = "Đơn hàng con trong chi tiết đơn hàng không được null")
    @JsonIgnore
    private SubOrderDomain subOrder;
}
