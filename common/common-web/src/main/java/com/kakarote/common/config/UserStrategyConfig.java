package com.kakarote.common.config;

import com.kakarote.common.servlet.UserStrategy;
import com.kakarote.common.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 用户策略配置类，在 Spring 启动时初始化 UserUtil 的 UserStrategy
 */
@Configuration
public class UserStrategyConfig {

    @Autowired
    private UserStrategy userStrategy;

    @PostConstruct
    public void init() {
        System.out.println("[UserStrategyConfig] Initializing UserUtil with UserStrategy: " + userStrategy.getClass().getName());
        UserUtil.setUserStrategy(userStrategy);
    }
}
