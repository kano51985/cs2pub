package com.sana.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public static class illegalRequestException extends BusinessException {
        public illegalRequestException(String message) {
            super(message);
        }
    }

    public static class RegularException extends BusinessException {
        public RegularException(String message) {
            super(message);
        }
    }
}
