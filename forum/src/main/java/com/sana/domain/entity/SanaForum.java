package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sana.domain.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛类
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sana_forum")
public class SanaForum extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String forumName;
    private String description;
    @TableLogic(value = "1", delval = "0")
    private int status;
}
