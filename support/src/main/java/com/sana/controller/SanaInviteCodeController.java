package com.sana.controller;


import com.sana.annotation.PermissionCheck;
import com.sana.domain.entity.SanaInviteCodes;
import com.sana.response.R;
import com.sana.service.ISanaInviteCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 邀请码控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("inviteCode")
public class SanaInviteCodeController {
    @Autowired
    private ISanaInviteCodesService sanaInviteCodesService;
    /**
     * 查询邀请码状态
     */
    @PermissionCheck(value = {"admin", "stuff"}, logical = PermissionCheck.Logical.OR)
    @GetMapping("{inviteCode}")
    public R getInviteCodeStatus(String inviteCode) {
        SanaInviteCodes.InviteCodeEnum inviteCodeEnum = sanaInviteCodesService.checkInviteCodeStatus(inviteCode);
        return inviteCodeEnum != null? R.success(inviteCodeEnum) : R.error("邀请码不存在");
    }

    /**
     * 发放邀请码
     */
    @PermissionCheck("admin")
    @PostMapping
    public R deleverInviteCode(@RequestBody String userIds) {
        boolean flag = sanaInviteCodesService.sendInviteCode(userIds);
        return flag? R.success("邀请码发放成功") : R.error("邀请码发放失败");
    }

}
