package com.ecommerce.studentmarket.student.store.repositories;

import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreDomain, Long> {
    StoreDomain findByStudent_MssvAndStudent_TrangThai(String mssv, TrangThai trangThai);
}
