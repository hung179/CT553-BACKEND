package com.ecommerce.studentmarket.product.repositories;

import com.ecommerce.studentmarket.product.domains.ProductDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDomain, Long> {

    ProductDomain findByMaSPAndDaXoaFalse(Long maSP);
//  Tìm tất cả sản phẩm trừ sản phẩm đã bị xóa và bị ẩn
    Page<ProductDomain> findByDaXoaFalseAndDaAnFalse(Pageable pageable);
//  Tìm theo tên sản phẩm trừ sản phẩm đã bị xóa và bị ẩn
    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndDaXoaFalseAndDaAnFalse(String tenSP, Pageable pageable);
//  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm đã xóa
    Page<ProductDomain> findByMaGHSHAndDaXoaFalse(Long maGHSH, Pageable pageable);
//  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm bị xóa hoặc bị ẩn
    Page<ProductDomain> findByMaGHSHAndDaXoaFalseAndDaAnFalse(Long maGHSH, Pageable pageable);
}
