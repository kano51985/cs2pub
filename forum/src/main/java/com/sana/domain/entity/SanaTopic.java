package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴实体类
 * @Version: 1.0
 */
@Data
@TableName("sana_topic")
public class SanaTopic implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String forumId;
    private String topicName;
    @TableLogic
    private int status;
    private String remark;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
