/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.context.annotation.Import
 */
package com.kakarote.common.config;

import com.kakarote.common.config.MybatisPlusConfig;
import com.kakarote.common.config.SwaggerConfig;
import com.kakarote.common.config.WebConfig;
import com.kakarote.common.redis.RedisImpl;
import com.kakarote.common.servlet.ApplicationContextHolder;
import com.kakarote.common.servlet.BaseMetaObjectHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value={RedisImpl.class, ApplicationContextHolder.class, MybatisPlusConfig.class, BaseMetaObjectHandler.class, SwaggerConfig.class, WebConfig.class})
public class CommonWebAutoConfiguration {
}

