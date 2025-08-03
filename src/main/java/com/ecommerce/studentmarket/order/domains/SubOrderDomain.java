package com.ecommerce.studentmarket.order.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DON_HANG_CON")
public class SubOrderDomain {
    @Id
    @NotNull(message = "Mã đơn hàng con không được bỏ trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subOrderSeq")
    @SequenceGenerator(
            name = "subOrderSeq",
            sequenceName = "subOrderIdSeq",
            allocationSize = 1000000
    )
    private Long maDHC;

    @NotNull(message = "Mã gian hàng sở hữu đơn hàng con không được trống")
    private Long maGianHangDHC;

    @OneToMany(mappedBy = "subOrder", cascade = CascadeType.ALL)
    private List<OrderItemDomain> items;

    @OneToOne(mappedBy = "subOrder", cascade = CascadeType.ALL)
    private OrderStateDomain orderState;

    @ManyToOne
    @JoinColumn(name = "maDH")
    @NotNull(message = "Đơn hàng trong đơn hàng con không được null")
    @JsonIgnore
    private OrderDomain orderDomain;
}
