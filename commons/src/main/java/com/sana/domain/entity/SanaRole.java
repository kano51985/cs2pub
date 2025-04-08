package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-07
 * @Description: 角色类
 * @Version: 1.0
 */

@TableName("sana_role")
public class SanaRole {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String role;
    private String roleName;
}
