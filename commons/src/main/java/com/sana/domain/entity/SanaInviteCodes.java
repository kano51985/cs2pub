package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 邀请码类
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
@TableName("sana_invitecodes")
public class SanaInviteCodes implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String inviteCode;
    private String belong;
    private LocalDateTime deleverdTime;
    private LocalDateTime expireTime;
    private InviteCodeEnum status;
    private String creator;


    @Getter
    public enum InviteCodeEnum {
        /**
         * 邀请码状态
         */
        active("可用"),
        used("已使用"),
        expired("已过期");
        private final String description;

        InviteCodeEnum(String description) {
            this.description = description;
        }
    }

}
