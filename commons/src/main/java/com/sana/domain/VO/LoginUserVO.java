package com.sana.domain.VO;


import com.sana.domain.entity.SanaUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: 登录用户VO
 * @Version: 1.0
 */

@Data
@Accessors(chain = true)
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private SanaUser user = new SanaUser();
    private String token;
    private long loginTime;
}
