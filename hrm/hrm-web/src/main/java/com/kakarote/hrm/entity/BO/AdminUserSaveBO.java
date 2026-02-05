package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 管理员保存请求对象
 */
@Data
@ApiModel(value = "AdminUserSaveBO", description = "管理员保存请求对象")
public class AdminUserSaveBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "员工ID不能为空")
    @ApiModelProperty(value = "员工ID", required = true)
    private Long employeeId;

    @NotNull(message = "角色ID列表不能为空")
    @ApiModelProperty(value = "角色ID列表", required = true)
    private List<Long> roleIds;
}
