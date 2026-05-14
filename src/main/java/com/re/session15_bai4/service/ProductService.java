package com.re.session15_bai4.service;

import com.re.session15_bai4.exception.*;
import com.re.session15_bai4.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public int applyBulkDiscount(String categoryName, Double discountPercentage) {

        // ── Bẫy 1: Kiểm tra % giảm giá hợp lệ ───────────────────────────
        // Chặn trước khi xuống DB, tuyệt đối không để DB nhận giá trị sai
        // discountPercentage <= 0 : số âm hoặc bằng 0 (không giảm gì)
        // discountPercentage > 100: giá sau giảm sẽ thành âm → vô nghĩa
        if (discountPercentage == null || discountPercentage <= 0 || discountPercentage > 100) {
            throw new InvalidDiscountException(
                    "Phần trăm giảm giá không hợp lệ: " + discountPercentage
                            + ". Giá trị phải nằm trong khoảng (0, 100]."
            );
        }

        // ── Bẫy 2 (phần 1): Kiểm tra categoryName không rỗng ────────────
        // Tránh trường hợp gửi câu UPDATE xuống DB với WHERE category = ''
        // làm ảnh hưởng ngoài ý muốn hoặc trả về 0 mà không rõ nguyên nhân
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException(
                    "Tên danh mục không được để trống."
            );
        }

        // ── Gọi Repository: 1 câu JPQL UPDATE duy nhất xuống DB ──────────
        int updatedCount = productRepository.bulkUpdatePrice(categoryName, discountPercentage);

        // ── Bẫy 2 (phần 2): Kiểm tra số sản phẩm được cập nhật ──────────
        // updatedCount == 0 có 2 nguyên nhân:
        //   a) categoryName không tồn tại trong DB
        //   b) categoryName có tồn tại nhưng tất cả sản phẩm đều INACTIVE
        // Cả 2 trường hợp đều phải báo lỗi rõ ràng thay vì "Cập nhật 0 sản phẩm"
        if (updatedCount == 0) {
            throw new CategoryNotFoundException(
                    "Không tìm thấy sản phẩm ACTIVE nào trong danh mục: '" + categoryName + "'."
                            + " Vui lòng kiểm tra lại tên danh mục."
            );
        }

        // Trả về số sản phẩm đã cập nhật → Controller hiển thị thông báo thành công
        return updatedCount;
    }
}