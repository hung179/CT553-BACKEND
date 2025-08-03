package com.ecommerce.studentmarket.student.ewallet.repositories;

import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionDomain, Long> {
}
