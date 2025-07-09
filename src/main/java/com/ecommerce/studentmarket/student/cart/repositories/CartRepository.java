package com.ecommerce.studentmarket.student.cart.repositories;

import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartDomain, Long> {

    CartDomain findByStudent_Mssv(String mssv);

}
