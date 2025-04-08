package com.sana.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionCheck {
    /**
     * 需要的角色列表
     */
    String[] value() default {};

    /**
     * 逻辑关系：AND表示需要所有角色，OR表示只需任一角色
     */
    Logical logical() default Logical.AND;

    enum Logical {
        AND, OR
    }
}
