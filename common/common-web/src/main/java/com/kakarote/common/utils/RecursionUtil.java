/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.bean.BeanUtil
 *  cn.hutool.core.map.MapProxy
 *  cn.hutool.core.map.MapUtil
 *  cn.hutool.core.util.ReflectUtil
 */
package com.kakarote.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapProxy;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.kakarote.common.exception.BusinessException;
import com.kakarote.common.result.SystemCodeEnum;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecursionUtil {
    public static <R> List<R> getChildList(List<R> allList, String parentName, Object parentId, String idName) {
        return RecursionUtil.getChildList(allList, parentName, parentId, idName, null, 20);
    }

    public static <R, T> List<T> getChildList(List<R> allList, String parentName, Object parentId, String idName, String returnName) {
        return RecursionUtil.getChildList(allList, parentName, parentId, idName, returnName, 20);
    }

    @SuppressWarnings("unchecked")
    public static <R, T> List<T> getChildList(List<R> allList, String parentName, Object parentId, String idName, String returnName, Integer depth) {
        Integer n = depth;
        depth = depth - 1;
        Integer n2 = depth;
        ArrayList<T> arrList = new ArrayList<T>();
        if (depth < 0) {
            return arrList;
        }
        for (Object r : allList) {
            if (r instanceof Map) {
                MapProxy proxy = MapUtil.createProxy((Map)((Map)r));
                if (!Objects.equals(parentId, proxy.get((Object)parentName))) continue;
                if (returnName == null) {
                    arrList.add((T) r);
                } else {
                    arrList.add((T) proxy.get((Object)returnName));
                }
                arrList.addAll(RecursionUtil.getChildList(allList, parentName, proxy.get((Object)idName), idName, returnName, depth));
                continue;
            }
            Field idField = ReflectUtil.getField(r.getClass(), (String)idName);
            Field parentField = ReflectUtil.getField(r.getClass(), (String)parentName);
            if (idField == null || parentField == null) {
                throw new BusinessException(SystemCodeEnum.SYSTEM_NO_SUCH_PARAMENT_ERROR);
            }
            if (!Objects.equals(parentId, ReflectUtil.getFieldValue(r, (Field)parentField))) continue;
            if (returnName == null) {
                arrList.add((T) r);
            } else {
                arrList.add((T) ReflectUtil.getFieldValue(r, (String)returnName));
            }
            arrList.addAll(RecursionUtil.getChildList(allList, parentName, ReflectUtil.getFieldValue(r, (Field)idField), idName, returnName, depth));
        }
        return arrList;
    }

    public static <R, T> List<T> getChildListTree(List<R> allList, String parentName, Object parentId, String idName, String treeName, Class<T> clazz) {
        return RecursionUtil.getChildListTree(allList, parentName, parentId, idName, treeName, clazz, 20);
    }

    @SuppressWarnings("unchecked")
    public static <R, T> List<T> getChildListTree(List<R> allList, String parentName, Object parentId, String idName, String treeName, Class<T> clazz, Integer depth) {
        Integer n = depth;
        depth = depth - 1;
        Integer n2 = depth;
        ArrayList<T> arrList = new ArrayList<T>();
        if (depth < 0) {
            return arrList;
        }
        for (Object data : allList) {
            Field idField = ReflectUtil.getField(data.getClass(), (String)idName);
            Field parentField = ReflectUtil.getField(data.getClass(), (String)parentName);
            if (idField == null || parentField == null) {
                throw new BusinessException(SystemCodeEnum.SYSTEM_NO_SUCH_PARAMENT_ERROR);
            }
            if (!Objects.equals(parentId, ReflectUtil.getFieldValue(data, (Field)parentField))) continue;
            Object properties = BeanUtil.copyProperties(data, clazz, (String[])new String[0]);
            ReflectUtil.setFieldValue((Object)properties, (String)treeName, RecursionUtil.getChildListTree(allList, parentName, ReflectUtil.getFieldValue(data, (Field)idField), idName, treeName, clazz, depth));
            arrList.add((T) properties);
        }
        return arrList;
    }
}

