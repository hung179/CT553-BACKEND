package com.ecommerce.studentmarket.admin.repositories;

import com.ecommerce.studentmarket.admin.domains.AdminDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminDomain, String> {

    boolean existsBySdt(String sdt);
    boolean existsBySdtAndMscbNot(String sdt, String mscb);
}
