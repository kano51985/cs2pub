package com.sana.exception;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String detail;

    public BusinessException(int code, String message) {
        this(code, message, null);
    }

    public BusinessException(int code, String message, String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    // getter方法
    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }
}
