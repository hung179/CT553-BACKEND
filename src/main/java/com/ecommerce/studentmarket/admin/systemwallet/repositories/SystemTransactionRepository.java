package com.ecommerce.studentmarket.admin.systemwallet.repositories;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemTransactionDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemTransactionRepository extends JpaRepository<SystemTransactionDomain, Long> {
    Page<SystemTransactionDomain> findAllBySystemWallet_MaVHT(Long maVHT, Pageable pageable);


}
