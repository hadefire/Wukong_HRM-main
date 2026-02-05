/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.models.auth.In
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.http.HttpMethod
 *  springfox.documentation.builders.ApiInfoBuilder
 *  springfox.documentation.builders.PathSelectors
 *  springfox.documentation.builders.RequestHandlerSelectors
 *  springfox.documentation.builders.ResponseBuilder
 *  springfox.documentation.oas.annotations.EnableOpenApi
 *  springfox.documentation.service.ApiInfo
 *  springfox.documentation.service.ApiKey
 *  springfox.documentation.service.Response
 *  springfox.documentation.service.SecurityScheme
 *  springfox.documentation.spi.DocumentationType
 *  springfox.documentation.spring.web.plugins.Docket
 */
package com.kakarote.common.config;

import com.kakarote.common.result.SystemCodeEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value(value="${spring.application.name:core}")
    private String desc;

    @Bean
    public Docket createRestApi() {
        ArrayList<Response> responseMessageList = new ArrayList<Response>();
        responseMessageList.add(new ResponseBuilder().code("200").description(SystemCodeEnum.SYSTEM_OK.getMsg()).build());
        responseMessageList.add(new ResponseBuilder().code("400").description(SystemCodeEnum.SYSTEM_NO_VALID.getMsg()).build());
        responseMessageList.add(new ResponseBuilder().code("401").description(SystemCodeEnum.SYSTEM_NO_AUTH.getMsg()).build());
        responseMessageList.add(new ResponseBuilder().code("403").description(SystemCodeEnum.SYSTEM_BAD_REQUEST.getMsg()).build());
        responseMessageList.add(new ResponseBuilder().code("500").description(SystemCodeEnum.SYSTEM_ERROR.getMsg()).build());
        Docket docket = new Docket(DocumentationType.OAS_30).apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build();
        try {
            Class<?> request = Class.forName("javax.servlet.http.HttpServletRequest");
            Class<?> response = Class.forName("javax.servlet.http.HttpServletResponse");
            docket.ignoredParameterTypes(new Class[]{request, response});
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        docket.useDefaultResponseMessages(false);
        docket.securitySchemes(this.securitySchemes());
        docket.globalResponses(HttpMethod.GET, responseMessageList);
        docket.globalResponses(HttpMethod.POST, responseMessageList);
        docket.globalResponses(HttpMethod.DELETE, responseMessageList);
        docket.globalResponses(HttpMethod.PUT, responseMessageList);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(this.desc).build();
    }

    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey("AUTH-TOKEN", "AUTH-TOKEN", In.HEADER.toValue());
        return Collections.singletonList(apiKey);
    }
}

