package com.kakarote.hrm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 显式绑定前端 dist 目录，避免 classpath:/static 干扰
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private static final String DIST_LOCATION =
            "file:C:/Users/hadef/Project/Wukong_HRM-main/ux/dist/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(DIST_LOCATION);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
