package com.ecommerce.studentmarket.student.ewallet.repositories;

import com.ecommerce.studentmarket.student.ewallet.domains.TransactionDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDomain, Long> {
    Page<TransactionDomain> findAllByWallet_MaVDT(Long maVDT, Pageable pageable);

    Boolean existsByIdGiaoDich(String idGiaoDich);

    TransactionDomain findByIdGiaoDich(String idGiaoDich);
}
