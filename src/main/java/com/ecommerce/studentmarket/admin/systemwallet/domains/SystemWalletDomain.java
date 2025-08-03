package com.ecommerce.studentmarket.student.ewallet.domains;


import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "VI_DIEN_TU")
public class EwalletDomain {
    @Id
    @NotNull(message = "Mã ví điện tử không được để trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "walletSeq")
    @SequenceGenerator(
            name = "walletSeq",
            sequenceName = "walletIdSeq",
            allocationSize = 1000000
    )
    private Long maVDT;

    private BigDecimal soDuVDT = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionDomain> transactions = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "mssv", referencedColumnName = "mssv", unique = true)
    @NotNull(message = "Sinh viên sở hữu ví không được để trống")
    @JsonIgnore
    private StudentDomain student;
}
