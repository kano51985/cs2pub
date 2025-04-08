package com.sana.constants;

public interface CacheConstants {
    // 认证后的用户key前缀
    String LOGIN_USER_KEY = "login_key";
    /**
     * 邮件验证码前缀
     */
    String EMAIL_VERIFY_KEY = "email_verify";
    /**
     * 邮件超时时间
     */
    Integer EMAIL_VERIFY_TIMEOUT = 5;

    /**
     * token有效时间(单位：分钟)
     */
    Integer TOKEN_VALID_TIME = 60;
}
