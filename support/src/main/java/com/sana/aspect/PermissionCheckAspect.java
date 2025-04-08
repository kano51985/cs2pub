package com.sana.aspect;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.sana.annotation.PermissionCheck;
import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import com.sana.exception.BusinessException;
import com.sana.exception.UserException;
import com.sana.utils.JwtUtils;
import com.sana.utils.RedisCacheUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: 权限校验切面类
 * @Version: 1.0
 */

@Component
public class PermissionCheckAspect implements Aspect {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisCacheUtil redisCacheUtil;


    @Override
    public boolean before(Object o, Method method, Object[] objects) {
        PermissionCheck permissionCheck = method.getAnnotation(PermissionCheck.class);
        // 没有注解直接放行
        if (permissionCheck == null) {
            return true;
        }
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String rawToken = request.getHeader("token");
        if (rawToken == null) {
            throw new BusinessException.illegalRequestException("未携带token的请求已丢弃");
        }
        // 解析请求头中拿到的token
        Claims parsedToken = jwtUtils.parseToken(rawToken);
        // 拼接token去redis拿到用户的元信息字符串
        String userInfoStr = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + parsedToken.getSubject());
        if (userInfoStr == null) {
            throw new UserException.UnauthorizedException("用户未登录或token已过期");
        }
        LoginUserVO currentUser = JSONUtil.toBean(userInfoStr, LoginUserVO.class);

        // 拿到注解标注要求的角色，进行对比
        String[] requiredRoles = permissionCheck.value();
        PermissionCheck.Logical logical = permissionCheck.logical();
        // 如果没有设置角色，则放行
        if (ArrayUtil.isEmpty(requiredRoles)) {
            return true;
        }
        // 拿到用户的角色
        List<String> userRoles = Arrays.stream(currentUser.getRole().split("-")).toList();
        if(CollUtil.isEmpty(userRoles)){
            throw new BusinessException.RegularException("用户未分配角色！");
        }
        if (logical == PermissionCheck.Logical.AND) {
            // 必须包含所有要求的角色
            if (!userRoles.containsAll(Arrays.asList(requiredRoles))) {
                throw new BusinessException.illegalRequestException("缺少必要角色权限");
            }
        } else {
            // 只需包含任一要求的角色
            boolean hasAnyRole = Arrays.stream(requiredRoles)
                    .anyMatch(userRoles::contains);
            if (!hasAnyRole) {
                throw new BusinessException.illegalRequestException("没有访问权限");
            }
        }
        return true;
    }

    @Override
    public boolean after(Object o, Method method, Object[] objects, Object o1) {
        return true;
    }


    @Override
    public boolean afterException(Object o, Method method, Object[] objects, Throwable throwable) {
        // todo 出现异常得日志记录一下具体情况
        return true;
    }
}
