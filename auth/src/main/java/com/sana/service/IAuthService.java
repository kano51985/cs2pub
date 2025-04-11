package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.dto.LoginDTO;

public interface IAuthService{
    String login(LoginDTO loginDTO);

}
