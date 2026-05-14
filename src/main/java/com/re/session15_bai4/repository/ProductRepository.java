package com.re.session15_bai4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Giải pháp 2 được chọn: Bulk UPDATE trực tiếp bằng JPQL
    //
    // @Modifying: báo Spring Data JPA đây là lệnh ghi (không phải SELECT)
    //   clearAutomatically = true → xóa Persistence Context (1st level cache)
    //   sau khi UPDATE để tránh Hibernate đọc phải dữ liệu cũ (stale data)
    //   trong cùng một transaction
    //
    // @Transactional: mở transaction để commit thay đổi xuống DB
    //
    // JPQL tính giá mới: price - (price * discount / 100)
    // Chỉ cập nhật sản phẩm status = 'ACTIVE', bỏ qua INACTIVE
    //
    // Trả về int: số dòng bị ảnh hưởng → dùng để phát hiện Bẫy 2
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.price = p.price - (p.price * :discount / 100) " +
            "WHERE p.category = :category " +
            "AND p.status = 'ACTIVE'")
    int bulkUpdatePrice(@Param("category") String category,
                        @Param("discount") Double discount);
}