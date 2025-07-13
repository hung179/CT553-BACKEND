package com.ecommerce.studentmarket.student.user.repositories;

import com.ecommerce.studentmarket.student.user.domains.StudentInvalidatedTokenDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInvalidatedTokenRepository extends JpaRepository<StudentInvalidatedTokenDomain, String> {
}
