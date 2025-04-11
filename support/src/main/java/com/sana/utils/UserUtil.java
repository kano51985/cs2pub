package com.sana.utils;


import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 用户工具类
 * @Version: 1.0
 */

@Component
public class UserUtil {
    private final RedisCacheUtil redisCacheUtil;
    private final JwtUtils jwtUtils;

    public UserUtil(RedisCacheUtil redisCacheUtil, JwtUtils jwtUtils) {
        this.redisCacheUtil = redisCacheUtil;
        this.jwtUtils = jwtUtils;
    }

    /**
     * 获取当前用户id
     */
    public String getUserId(String token) {
        Claims parsedToken = jwtUtils.parseToken(token);
        String tokenKey = (String) parsedToken.get("token");
        LoginUserVO loginUserVO = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + tokenKey);
        return loginUserVO.getUser().getId();
    }

    /**
     * 获取用户元信息
     */
    public LoginUserVO getCurrentUser(String token) {
        Claims parsedToken = jwtUtils.parseToken(token);
        String tokenKey = (String) parsedToken.get("token");
        LoginUserVO loginUserVO = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + tokenKey);
        return loginUserVO;
    }
}
