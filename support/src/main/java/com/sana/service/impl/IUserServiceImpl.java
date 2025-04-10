package com.sana.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.domain.entity.SanaUser;
import com.sana.mapper.UserMapper;
import com.sana.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 用户相关业务类
 * @Version: 1.0
 */

@Service
@Slf4j
public class IUserServiceImpl extends ServiceImpl<UserMapper, SanaUser> implements IUserService{
}
