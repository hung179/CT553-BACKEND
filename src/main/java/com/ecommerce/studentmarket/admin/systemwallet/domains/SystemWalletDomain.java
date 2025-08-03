package com.ecommerce.studentmarket.admin.systemwallet.domains;


import com.ecommerce.studentmarket.admin.user.domains.AdminDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "VI_HE_THONG")
public class SystemWalletDomain {
    @Id
    @NotNull(message = "Mã ví hệ thống không được để trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sysWalletSeq")
    @SequenceGenerator(
            name = "sysWalletSeq",
            sequenceName = "sysWalletIdSeq",
            allocationSize = 1000000
    )
    private Long maVHT;

    private BigDecimal soDuVHT = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "systemWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemTransactionDomain> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "systemWallet", cascade = CascadeType.ALL)
    private List<AdminDomain> admins = new ArrayList<>();
}
