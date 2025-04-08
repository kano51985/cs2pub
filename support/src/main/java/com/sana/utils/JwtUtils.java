package com.sana.utils;

import cn.hutool.core.util.StrUtil;
import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: JWT工具类
 * @Version: 1.0
 */

@Component
public class JwtUtils {
    private final String JWT_SIGN = "IDONTWANNAITWHATINEEDISLOV";
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private KeyUtil keyUtil;

    public String createToken(LoginUserVO loginUserVO) {
        String token = UUID.randomUUID().toString().replace("-", "");
        loginUserVO.setToken(token);
        loginUserVO.setLoginTime(System.currentTimeMillis());
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("token", token);
        // 刷新token
        refreshToken(loginUserVO);
        SecretKey sign = Keys.hmacShaKeyFor(JWT_SIGN.getBytes(StandardCharsets.UTF_8));
        Password encryptionKey = keyUtil.getEncryptSecret();
        // 选择密钥管理算法和内容加密算法
        KeyAlgorithm<Password, ?> keyAlg = Jwts.KEY.PBES2_HS256_A128KW;
        AeadAlgorithm encAlg = Jwts.ENC.A256GCM;
        return Jwts.builder()
                .claims(claims)
                .signWith(sign)
                .encryptWith(encryptionKey, encAlg)
                .compact();
    }

    public Claims parseToken(String token) {
        JwtParserBuilder parser = Jwts.parser();
        parser.setSigningKey(JWT_SIGN);
        parser.decryptWith(keyUtil.getEncryptSecret());
        return parser.build().parseClaimsJws(token).getBody();
    }

    public LoginUserVO getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(StrUtil.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            String parsedToken = (String) claims.get("token");
            LoginUserVO loginUserVO = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + parsedToken);
            if(loginUserVO != null) {
                long loginTime = loginUserVO.getLoginTime();
                long currentTimeMillis = System.currentTimeMillis();
                // 判断是否相差 20 分钟
                long mills = currentTimeMillis / 1000 / 60 - loginTime / 1000 / 60;
                if (mills >= 20) {
                    // 刷新token
                    refreshToken(loginUserVO);
                }
                return loginUserVO;
            }
        }
        return null;
    }

    // 刷新token
    private void refreshToken(LoginUserVO loginUserVO) {
        // 将用户数据存储到 Redis 中                             key：token   value：loginUserVO
        redisCacheUtil.setCacheObject(CacheConstants.LOGIN_USER_KEY + loginUserVO.getToken(),loginUserVO,CacheConstants.TOKEN_VALID_TIME, TimeUnit.MINUTES);
    }
}
