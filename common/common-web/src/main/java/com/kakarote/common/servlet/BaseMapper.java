/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.mapper.BaseMapper
 *  org.apache.ibatis.annotations.Param
 */
package com.kakarote.common.servlet;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T>
extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    default public T selectOne(@Param(value="ew") Wrapper<T> queryWrapper) {
        List dataList = this.selectList(queryWrapper);
        if (dataList.isEmpty()) {
            return null;
        }
        return (T)dataList.iterator().next();
    }
}

