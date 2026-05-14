package com.re.session15_bai4.exception;

// Ném ra khi Admin nhập % giảm giá không hợp lệ (âm hoặc > 100)
public class InvalidDiscountException extends RuntimeException {
    public InvalidDiscountException(String message) {
        super(message);
    }
}
