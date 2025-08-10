package com.ecommerce.studentmarket.order.domains;


import com.ecommerce.studentmarket.order.Enums.ThanhToan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DON_HANG")
public class OrderDomain {
    @Id
    @NotNull(message = "Mã đơn hàng không được bỏ trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @SequenceGenerator(
            name = "orderSeq",
            sequenceName = "orderIdSeq",
            allocationSize = 1000000
    )
    private Long maDH;

    @NotBlank(message = "Mã sinh viên sở hữu đơn hàng không được bỏ trống")
    private String mssvDH;

    @NotNull(message = "Ngày tạo đơn hàng không được để trống")
    private LocalDateTime thoiGianTaoDH;

    @NotNull(message = "Tổng số tiền đơn hàng không được để trống")
    private BigDecimal tongTienDH;

    @NotNull(message = "Trạng thái thanh toán đơn hàng không được bỏ trống")
    private ThanhToan thanhToan = ThanhToan.CHUATHANHTOAN;

    private LocalDateTime thoiGianThanhToanDH;

    @OneToMany(mappedBy = "orderDomain", cascade = CascadeType.ALL)
    private List<SubOrderDomain> subOrders = new ArrayList<>();

    @OneToOne(mappedBy = "orderDomain", cascade = CascadeType.ALL)
    private AddressOrderDomain addressOrderDomain;
}
