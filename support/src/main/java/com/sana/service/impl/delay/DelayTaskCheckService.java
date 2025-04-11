package com.sana.service.impl.delay;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sana.domain.entity.SanaInviteCodes;
import com.sana.domain.entity.delay.DelayedTask;
import com.sana.mapper.SanaInviteCodesMapper;
import com.sana.mapper.delay.DelayedTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-12
 * @Description: 延迟任务定时检查类
 * @Version: 1.0
 */
@Slf4j
@Service
public class DelayTaskCheckService {
    @Autowired
    private DelayedTaskMapper delayedTaskMapper;
    @Autowired
    private SanaInviteCodesMapper inviteCodesMapper;
    @Autowired
    private RedisDelayQueueService redisDelayQueueService;

    /**
     * 定时检查未处理的任务(兜底机制)
     * 每5分钟执行一次
     */
    /**
     * 专门检查邀请码过期任务
     */
    @Scheduled(cron = "1 * * * * ?")
    public void checkExpiredInviteCodes() {
        // 查询已过期但状态仍为AVAILABLE的邀请码
        List<SanaInviteCodes> expiredCodes = inviteCodesMapper.selectList(
                new LambdaQueryWrapper<SanaInviteCodes>()
                        .eq(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.active)
                        .lt(SanaInviteCodes::getExpireTime, LocalDateTime.now())
        );

        if (!expiredCodes.isEmpty()) {
            log.warn("发现{}个已过期但未处理的邀请码", expiredCodes.size());
            expiredCodes.forEach(code -> {
                // 直接更新状态
                code.setStatus(SanaInviteCodes.InviteCodeEnum.expired);
                inviteCodesMapper.updateById(code);

                // 可以添加额外的过期处理逻辑
                log.info("强制更新邀请码状态为过期: {}", code.getInviteCode());
            });
        }
    }
}
