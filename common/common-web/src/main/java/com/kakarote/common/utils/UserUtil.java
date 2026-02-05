/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.crypto.SecureUtil
 */
package com.kakarote.common.utils;

import cn.hutool.crypto.SecureUtil;
import com.kakarote.common.entity.UserInfo;
import com.kakarote.common.servlet.UserStrategy;

public class UserUtil {
    private static UserStrategy userStrategy;

    public static void setUserStrategy(UserStrategy userStrategy) {
        UserUtil.userStrategy = userStrategy;
    }

    public static String sign(String username, String password, String salt) {
        return SecureUtil.md5((String)(username + password + "wukong".concat(salt)));
    }

    public static boolean verify(String username, String password, String salt, String sign) {
        return sign.equals(UserUtil.sign(username, password, salt));
    }

    public static void setUser(UserInfo user) {
        if (userStrategy != null) {
            userStrategy.setUser(user);
        }
    }

    public static void setUser(Long userId) {
        if (userStrategy != null) {
            userStrategy.setUser(userId);
        }
    }

    public static UserInfo getUser() {
        if (userStrategy != null) {
            return userStrategy.getUser();
        }
        return null;
    }

    public static Long getUserId() {
        UserInfo user = UserUtil.getUser();
        return user != null ? user.getUserId() : null;
    }

    public static Long getPoolId() {
        UserInfo user = UserUtil.getUser();
        return user != null ? user.getPoolId() : null;
    }

    public static void removeUser() {
        if (userStrategy != null) {
            userStrategy.removeUser();
        }
    }

    public static boolean isAdmin() {
        UserInfo user = UserUtil.getUser();
        return user != null && user.isAdmin();
    }
}

