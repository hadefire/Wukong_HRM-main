package com.kakarote.hrm.config;

import com.kakarote.common.config.CommonWebAutoConfiguration;
import com.kakarote.common.config.MybatisPlusConfig;
import com.kakarote.common.config.SwaggerConfig;
import com.kakarote.common.config.WebConfig;
import com.kakarote.common.servlet.ApplicationContextHolder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = {"com.kakarote.common"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        CommonWebAutoConfiguration.class,
                        MybatisPlusConfig.class,
                        SwaggerConfig.class,
                        WebConfig.class,
                        ApplicationContextHolder.class
                }
        )
)
public class CommonModuleConfig {
}
