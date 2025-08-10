package com.ecommerce.studentmarket.order.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TRANG_THAI_DON_HANG")
public class OrderStateDomain {
    @Id
    @NotNull(message = "Mã đơn hàng không được bỏ trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderStateSeq")
    @SequenceGenerator(
            name = "orderStateSeq",
            sequenceName = "orderStateIdSeq",
            allocationSize = 1000000
    )
    private Long maTTDH;

    private LocalDateTime choDuyetTTDH;

    private LocalDateTime xacNhanTTDH;

    private LocalDateTime dangGiaoTTDH;

    private LocalDateTime daGiaoTTDH;

    private LocalDateTime daNhanTTDH;

    private LocalDateTime daHuyTTDH;

    private LocalDateTime daHoanTienTTDH;

    @OneToOne
    @JoinColumn(name = "maDHC")
    @NotNull(message = "Đơn hàng con trong trạng thái đơn hàng con không được null")
    @JsonIgnore
    private SubOrderDomain subOrder;

}
