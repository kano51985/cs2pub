package com.sana.service.impl;


import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import com.sana.domain.dto.LoginDTO;
import com.sana.domain.entity.SanaUser;
import com.sana.mapper.UserMapper;
import com.sana.service.IAuthService;
import com.sana.service.IUserService;
import com.sana.utils.JwtUtils;
import com.sana.utils.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 认证业务类
 * @Version: 1.0
 */

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IAuthServiceImpl implements IAuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(LoginDTO loginDTO) {
        // 默认用户名登录
        int accountType = -1;
        // TODO 正则判定是否是邮箱或者手机，修改accountType以实现动态登录
        SanaUser baseUser = userMapper.selectByUsername(loginDTO.getAccount(), accountType);
        // 存储用户信息到redis(我似乎得把逻辑写道Utils里？）
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUser(baseUser);
        return jwtUtils.createToken(loginUserVO);
    }
}
