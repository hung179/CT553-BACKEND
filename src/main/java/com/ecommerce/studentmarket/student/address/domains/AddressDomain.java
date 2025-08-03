package com.ecommerce.studentmarket.student.address.domains;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "student")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DIA_CHI")
public class AddressDomain {
    @Id
    @NotNull(message = "Mã địa chỉ không được để trống")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSeq")
    @SequenceGenerator(
            name = "addressSeq",
            sequenceName = "addressIdSeq",
            allocationSize = 1000000
    )
    private Long maDC;

    private String tinhDC;

    private String huyenDC;

    private String xaDC;

    private String chiTietDC;

    private Boolean macDinhDC;

    @ManyToOne
    @JoinColumn(name = "mssv", referencedColumnName = "mssv")
    @NotNull(message = "Sinh viên sở hữu địa chỉ không được để trống")
    @JsonIgnore
    private StudentDomain student;
}
