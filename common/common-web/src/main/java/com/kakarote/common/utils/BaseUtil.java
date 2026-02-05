/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.lang.Snowflake
 *  cn.hutool.core.util.IdUtil
 */
package com.kakarote.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class BaseUtil {
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake();

    public static Long getNextId() {
        return SNOWFLAKE.nextId();
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}

