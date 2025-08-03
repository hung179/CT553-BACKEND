package com.ecommerce.studentmarket.order.repositories;

import com.ecommerce.studentmarket.order.domains.OrderDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDomain, Long> {

    Page<OrderDomain> findByMssvDH(String mssvDH, Pageable pageable);

}
