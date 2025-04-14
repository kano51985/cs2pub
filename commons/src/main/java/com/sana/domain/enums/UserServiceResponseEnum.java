package com.sana.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserServiceResponseEnum {
    SUCCESS(1, "注册成功"),
    FAILURE(0, "注册失败!"),
    USERNAME_DUPLICATE(2, "用户名重复!"),
    EMAIL_VERIFY_FAILED(3, "邮箱验证失败!"),
    INVITATION_CODE_INVALID(4, "邀请码无效或已使用!"),
    EMAIL_DUPLICATE(5,"邮箱重复");
    private final int code;
    private final String message;
}
