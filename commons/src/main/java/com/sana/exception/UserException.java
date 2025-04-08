package com.sana.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    /**
     * token异常
     */
    public static class UnauthorizedException extends UserException {
        public UnauthorizedException(String message) {
            super("用户未登录或token已过期");
        }
    }
    /**
     * 用户未找到异常
     */
    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(String message) {
            super("未找到符合条件的用户！");
        }
    }
}
