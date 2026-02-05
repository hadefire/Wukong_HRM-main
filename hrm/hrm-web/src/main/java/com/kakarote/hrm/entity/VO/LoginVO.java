package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录响应结果
 */
@Data
@ApiModel(value = "登录响应结果", description = "登录响应结果")
public class LoginVO {

    @ApiModelProperty(value = "访问令牌")
    private String token;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "员工ID")
    private Long employeeId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String realname;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "职位")
    private String post;
}
