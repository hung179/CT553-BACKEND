package com.ecommerce.studentmarket.order.repositories;

import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.OrderStateDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStateRepository extends JpaRepository<OrderStateDomain, Long> {
    OrderStateDomain findBySubOrder_MaDHC(Long maDHC);
}
