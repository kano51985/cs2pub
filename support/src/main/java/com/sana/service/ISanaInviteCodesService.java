package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.SanaInviteCodes;

public interface ISanaInviteCodesService extends IService<SanaInviteCodes> {
    SanaInviteCodes.InviteCodeEnum checkInviteCodeStatus(String inviteCode);

    boolean sendInviteCode(String userIds);

    boolean consumeInviteCode(String userIds, String inviteCode);

    void handleInviteCodeExpiration(String taskContent);
}
