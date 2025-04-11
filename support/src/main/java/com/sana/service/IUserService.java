package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.SanaUser;

public interface IUserService extends IService<SanaUser> {
    boolean auditUserStatus(String userId, SanaUser.UserStatus status);
}
