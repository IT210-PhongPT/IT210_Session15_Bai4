package com.re.session15_bai4.exception;

// Ném ra khi không tìm thấy sản phẩm ACTIVE nào trong danh mục được nhập
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
