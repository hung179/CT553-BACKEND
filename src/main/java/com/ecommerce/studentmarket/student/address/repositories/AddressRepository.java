package com.ecommerce.studentmarket.student.address.repositories;

import com.ecommerce.studentmarket.student.address.domains.AddressDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressDomain, Long> {

    Page<AddressDomain> findByStudent_Mssv(String mssv, Pageable pageable);

    Long countByStudent_Mssv(String mssv);

    List<AddressDomain> findByStudent_Mssv(String mssv);
}
