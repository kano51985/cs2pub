package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-07
 * @Description: 用户类
 * @Version: 1.0
 */
@Data
@TableName("sana_user")
public class SanaUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String account;
    private String nickname;
    private String password;
    private String avatar;
    private String email;
    private UserStatus status;
    private String inviterId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<SanaRole> roleList = new ArrayList<>();

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
