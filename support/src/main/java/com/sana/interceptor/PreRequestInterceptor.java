package com.sana.interceptor;


import cn.hutool.core.util.StrUtil;
import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import com.sana.utils.JwtUtils;
import com.sana.utils.RedisCacheUtil;
import com.sana.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 全局前置拦截器
 * @Version: 1.0
 */

@Component
public class PreRequestInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 1. 解析Token
            String rawToken = request.getHeader("token");
            if (StrUtil.isNotBlank(rawToken)) {
                Claims claims = jwtUtils.parseToken(rawToken);
                String parsedToken = (String) claims.get("token");
                System.out.println("Parsed token: " + parsedToken); // Debug log

                // 2. 从Redis获取用户信息
                LoginUserVO loginUserVo = redisCacheUtil.getCacheObject(
                        CacheConstants.LOGIN_USER_KEY + parsedToken
                );
                System.out.println("Login user from Redis: " + loginUserVo); // Debug log

                // 3. 存入ThreadLocal
                if (loginUserVo != null) {
                    UserContext.setUser(loginUserVo);
                    return true;
                }
            }
        } finally {
            // 5. 异常后清除ThreadLocal 防止内存泄漏
            UserContext.clear();
        }
        throw new RuntimeException("请重新登录");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求处理完毕，清除ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}
