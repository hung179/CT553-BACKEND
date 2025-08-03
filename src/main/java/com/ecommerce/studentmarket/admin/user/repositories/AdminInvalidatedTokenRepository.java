package com.ecommerce.studentmarket.admin.repositories;

import com.ecommerce.studentmarket.admin.domains.AdminInvalidatedTokenDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInvalidatedTokenRepository extends JpaRepository<AdminInvalidatedTokenDomain, String> {
}
