package com.sana.domain.VO;


import com.sana.domain.entity.SanaUser;
import lombok.Data;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: 登录用户VO
 * @Version: 1.0
 */

@Data
public class LoginUserVO {
    private SanaUser user = new SanaUser();
    private String token;
    private String role;
    private long loginTime;
}
