package com.ecommerce.studentmarket.order.repositories;

import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.OrderItemDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemDomain, Long> {
}
