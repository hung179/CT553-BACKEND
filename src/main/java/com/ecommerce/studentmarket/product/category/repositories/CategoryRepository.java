package com.ecommerce.studentmarket.product.category.repositories;

import com.ecommerce.studentmarket.product.category.domains.CategoryDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryDomain, Long> {
    boolean existsByTenDMContainingIgnoreCase(String tenDM);

    @Query("SELECT c FROM CategoryDomain c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<CategoryDomain> findByIdWithProducts(@Param("id") Long id);
}
