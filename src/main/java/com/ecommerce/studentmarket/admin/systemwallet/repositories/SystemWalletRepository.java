package com.ecommerce.studentmarket.student.ewallet.repositories;

import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EwalletRepository extends JpaRepository<EwalletDomain, Long> {
    EwalletDomain findByStudent_Mssv(String mssv);
}
