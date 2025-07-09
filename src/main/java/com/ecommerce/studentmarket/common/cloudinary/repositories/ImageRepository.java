package com.ecommerce.studentmarket.common.cloudinary.repositories;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageDomain, Long> {
    List<ImageDomain> findByIdChuSoHuuAndChuSoHuuAndLoaiAnhAndDaXoaFalseOrderByThuTu(
            Long idChuSoHuu, ChuSoHuu chuSoHuu, LoaiAnh loaiAnh);

    List<ImageDomain> findByIdChuSoHuuAndChuSoHuuAndDaXoaFalseOrderByThuTu(Long maSH, ChuSoHuu chuSoHuu);
}
