package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-07
 * @Description: 用户角色关联表
 * @Version: 1.0
 */

@TableName("sana_user_role")
public class SanaUserRole {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String userId;
    private String roleId;
}
