package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员查询请求对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AdminUserQueryBO", description = "管理员查询请求对象")
public class AdminUserQueryBO extends PageEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "搜索关键字（姓名/手机号）")
    private String keyword;

    @ApiModelProperty(value = "角色类型（1=系统管理角色，3=人事角色）")
    private Integer roleType;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;
}
