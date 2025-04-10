package com.sana.service;

import com.sana.domain.dto.LoginDTO;

public interface IAuthService{
    String login(LoginDTO loginDTO);
}
