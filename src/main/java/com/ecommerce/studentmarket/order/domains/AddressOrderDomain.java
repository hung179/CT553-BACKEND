package com.ecommerce.studentmarket.order.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DIA_CHI_DON_HANG")
public class AddressOrderDomain {

    @Id
    @NotNull(message = "Mã địa chỉ không được để trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressOrderSeq")
    @SequenceGenerator(
            name = "addressOrderSeq",
            sequenceName = "addressOrderIdSeq",
            allocationSize = 1000000
    )
    private Long maDCDH;

    private String tinhDCDH;

    private String huyenDCDH;

    private String xaDCDH;

    private String chiTietDCDH;

    @OneToOne
    @JoinColumn(name = "maDH", referencedColumnName = "maDH")
    @NotNull(message = "Đơn hàng sở hữu địa chỉ không được để trống")
    @JsonIgnore
    private OrderDomain orderDomain;

}
