package com.ecommerce.studentmarket.student.cart.repositories;

import com.ecommerce.studentmarket.student.cart.domains.CartItemDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemDomain, CartItemIdDomain> {
    boolean existsByMaSP(Long maSP);
}
