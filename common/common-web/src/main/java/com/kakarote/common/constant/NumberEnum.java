/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IEnum
 */
package com.kakarote.common.constant;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum NumberEnum implements IEnum<Integer>
{
    NEGATIVE_ONE(-1),
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer number;

    private NumberEnum(Integer number) {
        this.number = number;
    }

    public Integer getValue() {
        return this.number;
    }
}

