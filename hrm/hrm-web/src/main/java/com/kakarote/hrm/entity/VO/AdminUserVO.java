package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员视图对象
 */
@Data
@ApiModel(value = "AdminUserVO", description = "管理员视图对象")
public class AdminUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工ID")
    private Long employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "职位")
    private String post;

    @ApiModelProperty(value = "角色列表")
    private List<AdminRoleVO> roles;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否超级管理员")
    private Boolean isSuperAdmin;

    @ApiModelProperty(value = "是否HRM管理员")
    private Boolean isHrmAdmin;

    /**
     * 角色视图对象
     */
    @Data
    @ApiModel(value = "AdminRoleVO", description = "角色视图对象")
    public static class AdminRoleVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "角色ID")
        private Long roleId;

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "角色类型")
        private Integer roleType;

        @ApiModelProperty(value = "数据权限")
        private Integer dataType;
    }
}
