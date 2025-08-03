package com.ecommerce.studentmarket.student.store.domains;

import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "GIAN_HANG_DIEN_TU")
public class StoreDomain {

    @Id
    @NotNull(message = "Mã gian hàng điện tử không được bỏ trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeSeq")
    @SequenceGenerator(
            name = "storeSeq",
            sequenceName = "storeIdSeq",
            allocationSize = 1000000
    )
    private Long maGHDT;

    private String moTaGHDT;

    @OneToOne
    @JoinColumn(name = "mssv", referencedColumnName = "mssv", unique = true)
    @NotNull(message = "Sinh viên sở hữu gian hàng không được để trống")
    @JsonIgnore
    private StudentDomain student;
}
