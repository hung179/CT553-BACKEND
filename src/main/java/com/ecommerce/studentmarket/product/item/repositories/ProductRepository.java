package com.ecommerce.studentmarket.product.item.repositories;

import com.ecommerce.studentmarket.product.item.domains.ProductDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDomain, Long> {

    ProductDomain findByMaSPAndDaXoaFalse(Long maSP);

    //Tìm tất cả các sản phẩm của danh mục trừ các sản phẩm của người sử dụng, bị xóa và bị ẩn
    Page<ProductDomain> findByCategory_MaDMAndDaXoaFalseAndDaAnFalseAndMaGHSHNotAndSoLuongGreaterThan(
            Long maDM,
            Long maGHSH,
            Long soLuong,
            Pageable pageable
    );

    Page<ProductDomain> findByDaXoaFalse(Pageable pageable);

    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndDaXoaFalse(String tenSP, Pageable pageable);

    ProductDomain findByMaSPAndDaAnFalseAndDaXoaFalseAndAndSoLuongGreaterThan(Long maSP, Long soLuong);

    //  Tìm tất cả sản phẩm trừ sản phẩm của người sử dụng, đã bị xóa và bị ẩn
    Page<ProductDomain> findByDaXoaFalseAndDaAnFalseAndMaGHSHNotAndSoLuongGreaterThan(Long maGHSH, Long soLuong ,Pageable pageable);

    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndDaXoaFalseAndDaAnFalseAndMaGHSHNotAndSoLuongGreaterThan(String tenSP, Long maGHSH, Long soLuong, Pageable pageable);

    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndCategory_MaDMAndDaXoaFalseAndDaAnFalseAndMaGHSHNotAndSoLuongGreaterThan(
            String tenSP,
            Long maDM,
            Long maGHSH,
            Long soLuong,
            Pageable pageable
    );

    //  Tìm theo tên sản phẩm trừ sản phẩm đã bị xóa và bị ẩn
    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndDaXoaFalseAndMaGHSHAndSoLuongGreaterThan(String tenSP, Long maGHSH, Long soLuong, Pageable pageable);

    //  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm đã xóa
    Page<ProductDomain> findByMaGHSHAndDaXoaFalse(Long maGHSH, Pageable pageable);
//  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm bị xóa hoặc bị ẩn
    Page<ProductDomain> findByMaGHSHAndDaXoaFalseAndDaAnFalse(Long maGHSH, Pageable pageable);
}
