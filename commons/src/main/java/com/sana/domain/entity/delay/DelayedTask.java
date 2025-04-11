package com.sana.domain.entity.delay;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-12
 * @Description: 延迟任务实体类
 * @Version: 1.0
 */

@Data
@TableName("delayed_tasks")
public class DelayedTask {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String taskType;
    private String taskContent;
    private LocalDateTime executeTime;
    private TaskStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Getter
    public enum TaskStatus {
        PENDING("等待执行"),
        PROCESSING("执行中"),
        COMPLETED("完成"),
        FAILED("失败");

        private final String value;

        TaskStatus(String value) {
            this.value = value;
        }
        // 添加toString方法确保存储到数据库的是枚举名称
        @Override
        public String toString() {
            return this.name();
        }
    }
}
