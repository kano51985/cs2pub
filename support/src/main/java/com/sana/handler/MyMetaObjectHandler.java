package com.sana.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sana.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component // 确保被 Spring 管理
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时自动填充 create_time 和 update_time
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "creator", String.class, UserContext.getUser().getUser().getId());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updater", String.class, UserContext.getUser().getUser().getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充 update_time
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updater", String.class, UserContext.getUser().getUser().getId());
    }
}