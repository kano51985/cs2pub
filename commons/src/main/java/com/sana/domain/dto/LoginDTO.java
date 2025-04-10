package com.sana.domain.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 登录请求类
 * @Version: 1.0
 */

@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String account;
    private String password;
    private boolean rememberMe;
}
