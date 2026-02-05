/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IEnum
 *  com.fasterxml.jackson.annotation.JsonCreator
 */
package com.kakarote.common.constant;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoginTypeEnum implements IEnum<Integer>
{
    USER(1),
    SMS_CODE(2),
    OAUTH2(11);

    private final Integer type;

    private LoginTypeEnum(Integer type) {
        this.type = type;
    }

    @JsonCreator
    public static LoginTypeEnum parse(Integer type) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (!value.getValue().equals(type)) continue;
            return value;
        }
        return USER;
    }

    public Integer getValue() {
        return this.type;
    }
}

