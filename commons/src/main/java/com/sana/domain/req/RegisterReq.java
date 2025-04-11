package com.sana.domain.req;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 注册请求类
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    private String account;
    private String nickname;
    private String password;
    private String email;
    private String inviteCode;
}
