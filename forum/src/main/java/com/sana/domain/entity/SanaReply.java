package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 帖子下的回复实体类
 * @Version: 1.0
 */
@Data
@TableName("table_reply")
public class SanaReply implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String topicId;
    private String content;
    private int floor;
    @TableLogic
    private int status;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
