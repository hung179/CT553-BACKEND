package com.ecommerce.studentmarket.order.repositories;

import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubOrderRepository extends JpaRepository<SubOrderDomain, Long> {
}
