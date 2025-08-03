package com.ecommerce.studentmarket.product.category.domains;

import com.ecommerce.studentmarket.product.item.domains.ProductDomain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DANH_MUC")
public class CategoryDomain {
    @Id
    @NotNull(message = "Mã danh mục không được để trống")
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorySeq")
    @SequenceGenerator(
            name = "categorySeq",
            sequenceName = "categoryIdSeq",
            allocationSize = 1000000
    )
    private Long maDM;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String tenDM;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDomain> products = new ArrayList<>();
}
