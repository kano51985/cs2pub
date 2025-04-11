package com.sana.utils;


import com.sana.domain.VO.LoginUserVO;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 用户上下文工具类
 * @Version: 1.0
 */


public class UserContext {
    private static final ThreadLocal<LoginUserVO> USER_HOLDER = new ThreadLocal<>();

    // 存储用户信息
    public static void setUser(LoginUserVO user) {
        USER_HOLDER.set(user);
    }

    // 获取用户信息
    public static LoginUserVO getUser() {
        return USER_HOLDER.get();
    }

    // 清除用户信息（防止内存泄漏）
    public static void clear() {
        USER_HOLDER.remove();
    }
}
