package com.sana.service.impl;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.annotation.PermissionCheck;
import com.sana.domain.entity.SanaInviteCodes;
import com.sana.domain.entity.delay.DelayedTask;
import com.sana.exception.BusinessException;
import com.sana.mapper.SanaInviteCodesMapper;
import com.sana.service.ISanaInviteCodesService;
import com.sana.service.impl.delay.RedisDelayQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 邀请码业务类
 * @Version: 1.0
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class SanaInviteCodeServiceImpl extends ServiceImpl<SanaInviteCodesMapper, SanaInviteCodes> implements ISanaInviteCodesService{
    @Autowired
    private SanaInviteCodesMapper sanaInviteCodesMapper;

    @Autowired
    @Lazy
    private RedisDelayQueueService redisDelayQueueService;

    /**
     * 检查邀请码状态
     */
    @PermissionCheck(value = {"admin","stuff"}, logical = PermissionCheck.Logical.OR)
    public SanaInviteCodes.InviteCodeEnum checkInviteCodeStatus(String inviteCode) {
        // 查询邀请码状态
        SanaInviteCodes inviteCodes = sanaInviteCodesMapper.selectOne(new LambdaQueryWrapper<SanaInviteCodes>()
                .eq(SanaInviteCodes::getInviteCode, inviteCode));
        if (ObjUtil.isEmpty(inviteCodes)){
            throw new BusinessException(510,"邀请码不存在");
        }
        // 检查邀请码是否在有效期
        LocalDateTime deleverdTime = inviteCodes.getDeleverdTime();
        LocalDateTime expireTime = inviteCodes.getExpireTime();
        // 邀请码到期前才允许注册，同时防止艾克？
        if (LocalDateTime.now().isAfter(deleverdTime) && LocalDateTime.now().isBefore(expireTime)){
            return inviteCodes.getStatus();
        }
        // 否则返回邀请码到期
        return SanaInviteCodes.InviteCodeEnum.expired;
    }

    /**
     * 发放邀请码
     * 注意：发放的同时还需要去设置定时任务
     */
    @Override
    public boolean sendInviteCode(String userIds) {
        // 通过 - 分隔用户id
        List<String> deleverdUserIds = Arrays.stream(userIds.split("-")).toList();
        // 插入邀请码
        List<SanaInviteCodes> inviteCodes = new ArrayList<>();
        deleverdUserIds.forEach(uid -> {
            String invCode = generateInviteCode();
            // 新建小对象，插入集合
            SanaInviteCodes sanaInvCode = new SanaInviteCodes()
                    .setInviteCode(invCode)
                    .setBelong(uid)
                    .setCreator(uid)
                    .setStatus(SanaInviteCodes.InviteCodeEnum.active)
                    .setExpireTime(LocalDateTime.now().plusSeconds(30))
                    .setDeleverdTime(LocalDateTime.now());
            inviteCodes.add(sanaInvCode);
            // 创建延迟任务使邀请码过期
            scheduleInviteCodeExpiration(sanaInvCode);
        });

        // 批量插入
        return this.saveBatch(inviteCodes);
    }
    /**
     * 使用邀请码
     */
    public boolean consumeInviteCode(String userIds, String inviteCode) {
        // 查询邀请码状态
        SanaInviteCodes inviteCodes = sanaInviteCodesMapper.selectOne(new LambdaQueryWrapper<SanaInviteCodes>()
                .eq(SanaInviteCodes::getInviteCode, inviteCode)
                .eq(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.active));

        if (ObjUtil.isEmpty(inviteCodes)) {
            throw new BusinessException(511, "邀请码不存在或已使用");
        }

        // 更新邀请码状态为 USED
        LambdaUpdateWrapper<SanaInviteCodes> updateWrapper = new LambdaUpdateWrapper<SanaInviteCodes>()
                .set(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.used)
                .eq(SanaInviteCodes::getInviteCode, inviteCode)
                .eq(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.active);

        int result = sanaInviteCodesMapper.update(null, updateWrapper);

        return result > 0;
    }

    /**
     * 生成邀请码
     */
    public String generateInviteCode() {
        // 生成随机邀请码
        String inviteCode = UUID.randomUUID().toString().replace("-", "");
        return inviteCode;
    }

    /**
     * 设置邀请码过期任务
     */
    private void scheduleInviteCodeExpiration(SanaInviteCodes inviteCode) {
        DelayedTask task = new DelayedTask();
        // 任务ID
        task.setId("expire:" + inviteCode.getInviteCode());
        task.setTaskType("INVITE_CODE_EXPIRE");
        task.setTaskContent(buildTaskContent(inviteCode));
        task.setExecuteTime(inviteCode.getExpireTime());
        task.setStatus(DelayedTask.TaskStatus.PENDING);

        redisDelayQueueService.addDelayedTask(task);
    }

    /**
     * 构建任务内容JSON
     */
    private String buildTaskContent(SanaInviteCodes inviteCode) {
        Map<String, Object> content = new HashMap<>();
        content.put("inviteCode", inviteCode.getInviteCode());
        content.put("belongUserId", inviteCode.getBelong());
        content.put("action", "EXPIRE");
        return JSONUtil.toJsonStr(content);
    }

    /**
     * 处理邀请码过期
     */
    public void handleInviteCodeExpiration(String taskContent) {
        try {
            Map<String, String> content = JSONUtil.toBean(taskContent, Map.class);
            String inviteCode = content.get("inviteCode");

            // 更新邀请码状态为过期
            lambdaUpdate()
                    .eq(SanaInviteCodes::getInviteCode, inviteCode)
                    .eq(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.active)
                    .set(SanaInviteCodes::getStatus, SanaInviteCodes.InviteCodeEnum.expired)
                    .update();

            // 这里可以添加其他逻辑，如通知用户等
            log.info("邀请码已过期: {}", inviteCode);

        } catch (Exception e) {
            log.error("处理邀请码过期失败: {}", taskContent, e);
        }
    }
}
