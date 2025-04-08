package com.sana.utils;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: 加密密钥工具类
 * @Version: 1.0
 */

@Component
public class KeyUtil {
    private final String JWT_ENCRYPT_SECRET = "U0VDUkVUS0VZQ0lUQURFTA==";

    public Password getEncryptSecret() {
        // 创建加密密钥
        byte[] keyBytes = Decoders.BASE64.decode(JWT_ENCRYPT_SECRET);
        char[] charArray = new String(keyBytes, StandardCharsets.UTF_8).toCharArray();
        return Keys.password(charArray);
    }
}
