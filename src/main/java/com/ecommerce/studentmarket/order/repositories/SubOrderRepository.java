package com.ecommerce.studentmarket.order.repositories;

import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubOrderRepository extends JpaRepository<SubOrderDomain, Long> {
    Page<SubOrderDomain> findByMaGianHangDHC(Long maGianHangDHC, Pageable pageable);

    @Query("SELECT s FROM SubOrderDomain s LEFT JOIN FETCH s.items WHERE s.maDHC = :maDHC")
    Optional<SubOrderDomain> findByIdWithItems(@Param("maDHC") Long maDHC);


    @EntityGraph(attributePaths = {"items"}) // tên thuộc tính bên trong entity SubOrderDomain
    Page<SubOrderDomain> findAll(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE s.maGianHangDHC = :maGianHangDHC
      AND st.choDuyetTTDH IS NOT NULL
      AND st.xacNhanTTDH IS NULL
      AND st.dangGiaoTTDH IS NULL
      AND st.daGiaoTTDH IS NULL
      AND st.daNhanTTDH IS NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findChoDuyetByMaGianHangDHC(@Param("maGianHangDHC") Long maGianHangDHC, Pageable pageable);


    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.choDuyetTTDH IS NOT NULL
      AND st.xacNhanTTDH IS NULL
      AND st.dangGiaoTTDH IS NULL
      AND st.daGiaoTTDH IS NULL
      AND st.daNhanTTDH IS NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllChoDuyet(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.xacNhanTTDH IS NOT NULL
      AND st.dangGiaoTTDH IS NULL
      AND st.daGiaoTTDH IS NULL
      AND st.daNhanTTDH IS NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllXacNhan(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.dangGiaoTTDH IS NOT NULL
      AND st.daGiaoTTDH IS NULL
      AND st.daNhanTTDH IS NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllDangGiao(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.daGiaoTTDH IS NOT NULL
      AND st.daNhanTTDH IS NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllDaGiao(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.daNhanTTDH IS NOT NULL
      AND st.daHuyTTDH IS NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllDaNhan(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.daHuyTTDH IS NOT NULL
      AND st.daHoanTienTTDH IS NULL
""")
    Page<SubOrderDomain> findAllDaHuy(Pageable pageable);

    @Query("""
    SELECT s FROM SubOrderDomain s
    JOIN s.orderState st
    WHERE st.daHoanTienTTDH IS NOT NULL
""")
    Page<SubOrderDomain> findAllDaHoanTien(Pageable pageable);
}
