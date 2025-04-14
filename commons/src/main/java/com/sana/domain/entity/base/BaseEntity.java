package com.sana.domain.entity.base;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 基础字段类
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 修改者
     */
    @TableField(value = "updater", fill = FieldFill.INSERT_UPDATE)
    private String updater;
}
