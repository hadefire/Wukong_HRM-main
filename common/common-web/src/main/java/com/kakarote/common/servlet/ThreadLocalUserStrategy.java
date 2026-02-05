package com.kakarote.common.servlet;

import com.kakarote.common.entity.UserInfo;
import org.springframework.stereotype.Component;

/**
 * 基于 ThreadLocal 的用户策略实现
 */
@Component
public class ThreadLocalUserStrategy implements UserStrategy {

    private static final ThreadLocal<UserInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public UserInfo getUser() {
        return USER_THREAD_LOCAL.get();
    }

    @Override
    public void setUser(UserInfo user) {
        USER_THREAD_LOCAL.set(user);
    }

    @Override
    public void setUser(Long userId) {
        // 如果需要按 userId 设置，可以从数据库或缓存获取用户信息
        // 这里简单处理，不支持此方法
    }

    @Override
    public void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}
