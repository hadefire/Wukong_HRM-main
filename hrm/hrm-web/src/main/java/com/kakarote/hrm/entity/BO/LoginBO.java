package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求参数
 */
@Data
@ApiModel(value = "登录请求参数", description = "登录请求参数")
public class LoginBO {

    @ApiModelProperty(value = "手机号/用户名", required = true)
    @NotBlank(message = "手机号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
