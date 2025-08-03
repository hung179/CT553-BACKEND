package com.ecommerce.studentmarket.product.items.repositories;

import com.ecommerce.studentmarket.product.items.domains.ProductDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDomain, Long> {

    ProductDomain findByMaSPAndDaXoaFalse(Long maSP);
    //  Tìm tất cả sản phẩm trừ sản phẩm của người sử dụng, đã bị xóa và bị ẩn
    Page<ProductDomain> findByDaXoaFalseAndDaAnFalseAndMaGHSHNot(Long maGHSH,Pageable pageable);
//  Tìm theo tên sản phẩm trừ sản phẩm đã bị xóa và bị ẩn
    Page<ProductDomain> findByTenSPContainingIgnoreCaseAndDaXoaFalseAndDaAnFalseAndMaGHSHNot(String tenSP, Long maGHSH, Pageable pageable);

    //  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm đã xóa
    Page<ProductDomain> findByMaGHSHAndDaXoaFalse(Long maGHSH, Pageable pageable);
//  Tìm các sản phẩm thuộc cửa hàng trừ sản phẩm bị xóa hoặc bị ẩn
    Page<ProductDomain> findByMaGHSHAndDaXoaFalseAndDaAnFalse(Long maGHSH, Pageable pageable);
}
