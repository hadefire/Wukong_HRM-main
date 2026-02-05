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

@ApiModel(value="\u7b80\u5355\u7684\u7528\u6237\u5bf9\u8c61")
public class SimpleUser
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Long userId;
    @ApiModelProperty(value="\u5934\u50cf")
    private String userImg;
    @ApiModelProperty(value="\u7528\u6237\u540d")
    private String username;
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    private String mobile;
    @ApiModelProperty(value="\u90ae\u7bb1")
    private String email;
    @ApiModelProperty(value="\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u7528\u6237\u72b6\u6001 0\u7981\u7528,1\u6b63\u5e38,2\u672a\u6fc0\u6d3b")
    private Integer status;
    @ApiModelProperty(value="\u90e8\u95e8ID")
    private Long deptId;
    @ApiModelProperty(value="\u90e8\u95e8\u540d\u79f0")
    private String deptName;

    public Long getUserId() {
        return this.userId;
    }

    public String getUserImg() {
        return this.userImg;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}

