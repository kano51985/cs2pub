package com.sana.controller.delay;


import com.sana.domain.entity.delay.DelayedTask;
import com.sana.service.impl.delay.RedisDelayQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-12
 * @Description: 延迟任务控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/delay")
public class DelayTaskController {

    @Autowired
    private RedisDelayQueueService redisDelayQueueService;

    @PostMapping("/add")
    public String addDelayTask(
            @RequestParam String taskType,
            @RequestParam String taskContent,
            @RequestParam LocalDateTime executeTime) {

        DelayedTask task = new DelayedTask();
        task.setTaskType(taskType);
        task.setTaskContent(taskContent);
        task.setExecuteTime(executeTime);
        task.setStatus(DelayedTask.TaskStatus.PENDING);

        redisDelayQueueService.addDelayedTask(task);
        return "延迟任务添加成功，任务ID: " + task.getId();
    }
}
