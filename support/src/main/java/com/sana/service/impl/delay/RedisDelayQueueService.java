package com.sana.service.impl.delay;


import cn.hutool.json.JSONUtil;
import com.sana.mapper.delay.DelayedTaskMapper;
import com.sana.service.ISanaInviteCodesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import com.sana.domain.entity.delay.DelayedTask;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-12
 * @Description: redis延迟任务类
 * @Version: 1.0
 */

@Service
@Slf4j
public class RedisDelayQueueService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private ISanaInviteCodesService inviteCodeService;
    @Autowired
    private DelayedTaskMapper delayedTaskMapper;
    private static final String DELAY_QUEUE_KEY = "delay:queue:tasks";

    /**
     * 添加延迟任务
     */
    public void addDelayedTask(DelayedTask task) {
        // 持久化到数据库
        delayedTaskMapper.insert(task);

        // 计算延迟时间(秒)
        long delaySeconds = Duration.between(LocalDateTime.now(), task.getExecuteTime()).getSeconds();

        if (delaySeconds <= 0) {
            // 立即执行
            executeTask(task);
            return;
        }

        // 添加到Redis延迟队列(ZSET)
        redisTemplate.opsForZSet().add(
                DELAY_QUEUE_KEY,
                task.getId(),
                System.currentTimeMillis() + delaySeconds * 1000
        );

        log.info("延迟任务已添加: {}, 执行时间: {}", task.getId(), task.getExecuteTime());
    }

    /**
     * 处理到期任务(每秒执行一次)
     */
    @Scheduled(fixedRate = 1000)
    public void processExpiredTasks() {
        long now = System.currentTimeMillis();

        // 获取所有到期的任务ID
        Set<Object> taskIds = redisTemplate.opsForZSet().rangeByScore(
                DELAY_QUEUE_KEY,
                0,
                now
        );

        if (taskIds == null || taskIds.isEmpty()) {
            return;
        }

        for (Object taskIdObj : taskIds) {
            String taskId = (String) taskIdObj;
            try {
                DelayedTask task = delayedTaskMapper.selectById(taskId);
                if (task == null || task.getStatus() != DelayedTask.TaskStatus.PENDING) {
                    // 无效任务，从队列移除
                    redisTemplate.opsForZSet().remove(DELAY_QUEUE_KEY, taskId);
                    continue;
                }

                executeTask(task);
                redisTemplate.opsForZSet().remove(DELAY_QUEUE_KEY, taskId);
            } catch (Exception e) {
                log.error("处理延迟任务失败: {}", taskId, e);
                // 标记为失败
                delayedTaskMapper.updateStatus(taskId, DelayedTask.TaskStatus.FAILED);
            }
        }
    }

    private void executeTask(DelayedTask task) {
        // 标记为处理中
        delayedTaskMapper.updateStatus(task.getId(), DelayedTask.TaskStatus.PROCESSING);

        try {
            // 实际业务逻辑
            handleTaskContent(task.getTaskContent());

            // 标记为已完成
            delayedTaskMapper.updateStatus(task.getId(), DelayedTask.TaskStatus.COMPLETED);
            log.info("任务执行成功: {}", task.getId());
        } catch (Exception e) {
            // 标记为失败
            delayedTaskMapper.updateStatus(task.getId(), DelayedTask.TaskStatus.FAILED);
            log.error("任务执行失败: {}", task.getId(), e);
            throw e; // 抛出异常以便外层捕获
        }
    }

    private void handleTaskContent(String content) {
        try {
            Map<String, Object> taskData = JSONUtil.toBean(content, Map.class);
            // 从内容中获取类型
            String taskType = (String) taskData.get("action");

            if ("EXPIRE".equals(taskType)) {
                // 处理邀请码过期
                inviteCodeService.handleInviteCodeExpiration(content);
            } else if ("OTHER_TYPE".equals(taskType)) {
                // 处理其他类型任务...
            }
        } catch (Exception e) {
            log.error("处理任务内容失败: {}", content, e);
            throw new RuntimeException("任务处理失败", e);
        }
    }
}
