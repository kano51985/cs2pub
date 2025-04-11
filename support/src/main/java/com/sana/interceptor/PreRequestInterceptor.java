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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 全局前置拦截器
 * @Version: 1.0
 */

@Component
public class PreRequestInterceptor extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                }
            }

            // 4. 放行请求
            filterChain.doFilter(request, response);
        } finally {
            // 5. 请求结束后清除ThreadLocal
            UserContext.clear(); // 防止内存泄漏
        }
    }
}
