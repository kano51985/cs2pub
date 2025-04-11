package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 帖子下的回复实体类
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sana_reply")
public class SanaReply extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String topicId;
    private String content;
    private int floor;
    @TableLogic(value = "1", delval = "0")
    private int status;
    private String creator;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
    private String updater;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;
}
