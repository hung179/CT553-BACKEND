package com.ecommerce.studentmarket.student.user.repositories;

import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentDomain, String> {
    boolean existsBySdt(String sdt);
    boolean existsBySdtAndMssvNot(String sdt, String mssv);
    Page<StudentDomain> findByTrangThaiAndHoTenContainingIgnoreCaseOrTrangThaiAndMssvContainingIgnoreCase(
            TrangThai trangThai1, String hoTen,
            TrangThai trangThai2, String mssv,
            Pageable pageable
    );
    StudentDomain findByStore_maGHDT(Long maGHDT);
}
