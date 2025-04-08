package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-07
 * @Description: 用户类
 * @Version: 1.0
 */
@Data
@TableName("sana_user")
public class SanaUser {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String account;
    private String nickname;
    private String password;
    private String avatar;
    private String email;
    private UserStatus status;
    private String inviterId;
    private Date createTime;
    private Date updateTime;

    @Getter
    public enum UserStatus {
        activated("启用"),
        restricted("受限（访客）"),
        suspended("正在调查"),
        banned("禁止");
        private final String description;
        UserStatus(String description) {this.description = description;}
    }
}
