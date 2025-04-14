package com.sana.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.domain.entity.SanaInviteCodes;
import com.sana.domain.entity.SanaUser;
import com.sana.domain.enums.UserServiceResponseEnum;
import com.sana.domain.req.RegisterReq;
import com.sana.mapper.UserMapper;
import com.sana.service.ISanaInviteCodesService;
import com.sana.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 用户相关业务类
 * @Version: 1.0
 */

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IUserServiceImpl extends ServiceImpl<UserMapper, SanaUser> implements IUserService{
    @Value("${sana.defaultAvatar}")
    private String defaultAvatar;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISanaInviteCodesService inviteCodesService;
    @Override
    public boolean auditUserStatus(String userId, SanaUser.UserStatus status) {
        SanaUser matchedUser = userMapper.selectById(userId);
        if (matchedUser != null){
            matchedUser.setStatus(status);
        }
        int i = userMapper.updateById(matchedUser);
        return i==1;
    }

    @Override
    public UserServiceResponseEnum register(RegisterReq user, String inviteCode) {
        // 检查邀请码是否有效
        SanaInviteCodes.InviteCodeEnum status = inviteCodesService.checkInviteCodeStatus(inviteCode);
        if (status != SanaInviteCodes.InviteCodeEnum.active){
            return UserServiceResponseEnum.INVITATION_CODE_INVALID;
        }
        // 更新邀请码状态
        boolean b = inviteCodesService.consumeInviteCode(user.getAccount(), inviteCode);
        if (!b){
            return UserServiceResponseEnum.FAILURE;
        }
        // 初始化用户信息
        LocalDateTime now = LocalDateTime.now();
        SanaUser initUser = new SanaUser();
        BeanUtils.copyProperties(user,initUser);
        initUser.setStatus(SanaUser.UserStatus.activated);
        initUser.setAvatar(defaultAvatar);
        initUser.setCreateTime(now);
        initUser.setUpdateTime(now);
        // 校验用户是否合法
        // 昵称或账号重名
        if (userMapper.selectOne(new LambdaQueryWrapper<SanaUser>().eq(SanaUser::getAccount,initUser.getAccount())) == null){
            return UserServiceResponseEnum.USERNAME_DUPLICATE;
        }
        if (userMapper.selectOne(new LambdaQueryWrapper<SanaUser>().eq(SanaUser::getEmail,initUser.getEmail())) == null){
            return UserServiceResponseEnum.EMAIL_DUPLICATE;
        }
        // 校验完毕，准备插入数据
        if (userMapper.insert(initUser) == 1){
            return UserServiceResponseEnum.SUCCESS;
        }
        return UserServiceResponseEnum.FAILURE;
    }
}
