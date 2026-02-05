/*
 * Decompiled with CFR 0.152.
 */
package com.kakarote.common.servlet;

import com.kakarote.common.entity.UserInfo;

public interface UserStrategy {
    public UserInfo getUser();

    public void setUser(UserInfo var1);

    public void setUser(Long var1);

    public void removeUser();
}

