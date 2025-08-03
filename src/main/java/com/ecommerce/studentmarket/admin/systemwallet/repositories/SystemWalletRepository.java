package com.ecommerce.studentmarket.admin.systemwallet.repositories;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemWalletRepository extends JpaRepository<SystemWalletDomain, Long> {
    Optional<SystemWalletDomain> findTopByOrderByMaVHTAsc();

}
