package com.ecommerce.studentmarket.student.cart.domains;


import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GIO_HANG")
public class CartDomain {
    @Id
    @NotNull(message = "Id giỏ hàng không được để trống")
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartSeq")
    @SequenceGenerator(
            name = "cartSeq",
            sequenceName = "cartIdSeq",
            allocationSize = 1
    )
    private Long idGioHang;

    @OneToOne
    @JoinColumn(name = "mssv", referencedColumnName = "mssv", unique = true)
    @NotNull(message = "Sinh viên sở hữu giỏ hàng không được null")
    @JsonIgnore
    private StudentDomain student;

    @OneToMany(mappedBy = "cartDomain", cascade = CascadeType.ALL)
    private List<CartItemDomain> items = new ArrayList<>();
}
