/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
 *  org.apache.ibatis.reflection.MetaObject
 *  org.springframework.stereotype.Component
 */
package com.kakarote.common.servlet;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kakarote.common.entity.UserInfo;
import com.kakarote.common.utils.UserUtil;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class BaseMetaObjectHandler
implements MetaObjectHandler {
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_TIME = "updateTime";
    public static final String FIELD_CREATE_USER = "createUserId";
    public static final String FIELD_UPDATE_USER = "updateUserId";

    public void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.strictInsertFill(metaObject, FIELD_CREATE_TIME, LocalDateTime.class, localDateTime);
        UserInfo user = UserUtil.getUser();
        if (user != null) {
            this.strictInsertFill(metaObject, FIELD_CREATE_USER, Long.class, user.getUserId());
            if (user.getPoolId() != null) {
                this.strictInsertFill(metaObject, "poolId", Long.class, user.getPoolId());
            }
        }
    }

    public MetaObjectHandler fillStrategy(MetaObject metaObject, String fieldName, Object fieldVal) {
        this.setFieldValByName(fieldName, fieldVal, metaObject);
        return this;
    }

    public void updateFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.strictUpdateFill(metaObject, FIELD_UPDATE_TIME, LocalDateTime.class, localDateTime);
        UserInfo user = UserUtil.getUser();
        if (user != null) {
            this.strictUpdateFill(metaObject, FIELD_UPDATE_USER, Long.class, user.getUserId());
        }
    }
}

