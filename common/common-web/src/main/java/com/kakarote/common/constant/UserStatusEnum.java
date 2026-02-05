/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IEnum
 */
package com.kakarote.common.constant;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum UserStatusEnum implements IEnum<Integer>
{
    DISABLED(0),
    NORMAL(1),
    NON_ACTIVATED(2);

    private final Integer status;

    private UserStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getValue() {
        return this.status;
    }

    public static UserStatusEnum parse(Integer status) {
        for (UserStatusEnum value : UserStatusEnum.values()) {
            if (!value.getValue().equals(status)) continue;
            return value;
        }
        return DISABLED;
    }
}

