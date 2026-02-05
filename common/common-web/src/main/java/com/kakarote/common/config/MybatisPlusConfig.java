/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
 *  com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
 *  com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory
 *  org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.context.annotation.Primary
 */
package com.kakarote.common.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.kakarote.common.utils.BaseUtil;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return i -> i.setObjectWrapperFactory((ObjectWrapperFactory)new MybatisMapWrapperFactory());
    }

    @Bean
    @Primary
    public IdentifierGenerator idGenerator() {
        return entity -> BaseUtil.getNextId();
    }
}

