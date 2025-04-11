package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 角色论坛关联类
 * @Version: 1.0
 */

@Data
@TableName("sana_role_forum")
public class SanaRoleForum {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String roleId;
    private String forumId;
}
