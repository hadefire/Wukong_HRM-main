/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.kakarote.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u90e8\u95e8\u5bf9\u8c61")
public class SimpleDept
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u90e8\u95e8ID")
    private Long deptId;
    @ApiModelProperty(value="\u4e0a\u7ea7\u90e8\u95e8ID")
    private Long parentId;
    @ApiModelProperty(value="\u540d\u79f0")
    private String deptName;
    @ApiModelProperty(value="\u4e0a\u7ea7\u90e8\u95e8\u540d\u79f0")
    private String parentDeptName;

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getParentDeptName() {
        return this.parentDeptName;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setParentDeptName(String parentDeptName) {
        this.parentDeptName = parentDeptName;
    }
}

