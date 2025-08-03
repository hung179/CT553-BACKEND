package com.ecommerce.studentmarket.order.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            allocationSize = 1
    )
    private Long maTTDH;

    private Date choDuyetTTDH;

    private Date xacNhanTTDH;

    private Date dangGiaoTTDH;

    private Date daGiaoTTDH;

    private Date daHuyTTDH;

    private Date daHoanTienTTDH;

    @ManyToOne
    @MapsId("maDHC")
    @JoinColumn(name = "maDHC")
    @NotNull(message = "Trạng thái đơn hàng trong đơn hàng con không được null")
    @JsonIgnore
    private SubOrderDomain subOrderDomain;

}
