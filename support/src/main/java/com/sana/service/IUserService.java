package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.SanaUser;
import com.sana.domain.enums.UserServiceResponseEnum;
import com.sana.domain.req.RegisterReq;

public interface IUserService extends IService<SanaUser> {
    boolean auditUserStatus(String userId, SanaUser.UserStatus status);

    UserServiceResponseEnum register(RegisterReq user, String inviteCode);
}
