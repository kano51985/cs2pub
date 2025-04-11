package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.sana.domain.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴实体类
 * @Version: 1.0
 */
@Data
@TableName("sana_topic")
public class SanaTopic extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String forumId;
    private String topicName;
    // 默认为1楼回复的内容
    private String content;
    @TableLogic(value = "1", delval = "0")
    private int status;
    private String remark;

}
