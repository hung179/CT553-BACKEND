package com.ecommerce.studentmarket.student.user.repositories;

import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentDomain, String> {
    boolean existsBySdt(String sdt);
    boolean existsBySdtAndMssvNot(String sdt, String mssv);
}
