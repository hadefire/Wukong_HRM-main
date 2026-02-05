/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.kakarote.common.entity;

import com.alibaba.fastjson.JSON;
import com.kakarote.common.constant.LoginTypeEnum;
import com.kakarote.common.constant.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value="\u7528\u6237\u4fe1\u606f")
public class UserInfo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Long userId = 0L;
    @ApiModelProperty(value="\u4e0a\u7ea7\u7528\u6237ID")
    private Long parentId;
    @ApiModelProperty(value="\u4e3b\u90e8\u95e8ID")
    private Long deptId;
    @ApiModelProperty(value="\u7528\u6237\u540d")
    private String username;
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    private String mobile;
    @ApiModelProperty(value="\u90ae\u7bb1")
    private String email;
    @ApiModelProperty(value="\u5934\u50cf")
    private String userImg;
    @ApiModelProperty(value="\u6635\u79f0")
    private String nickname = "";
    @ApiModelProperty(value="\u767b\u5f55\u7c7b\u578b")
    private LoginTypeEnum loginType;
    @ApiModelProperty(value="\u767b\u5f55\u65f6\u95f4")
    private LocalDateTime loginTime;
    @ApiModelProperty(value="\u7528\u6237\u72b6\u6001")
    private UserStatusEnum userStatus;
    @ApiModelProperty(value="\u7528\u6237\u6c60ID")
    private Long poolId;
    @ApiModelProperty(value="\u662f\u5426\u662f\u7ba1\u7406\u5458")
    private boolean admin;
    @ApiModelProperty(value="\u89d2\u8272\u5217\u8868")
    private List<Long> roles;

    public String toString() {
        return JSON.toJSONString((Object)this);
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public Long getDeptId() {
        return this.deptId;
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

    public String getUserImg() {
        return this.userImg;
    }

    public String getNickname() {
        return this.nickname;
    }

    public LoginTypeEnum getLoginType() {
        return this.loginType;
    }

    public LocalDateTime getLoginTime() {
        return this.loginTime;
    }

    public UserStatusEnum getUserStatus() {
        return this.userStatus;
    }

    public Long getPoolId() {
        return this.poolId;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public List<Long> getRoles() {
        return this.roles;
    }

    public UserInfo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserInfo setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public UserInfo setDeptId(Long deptId) {
        this.deptId = deptId;
        return this;
    }

    public UserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserInfo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public UserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserInfo setUserImg(String userImg) {
        this.userImg = userImg;
        return this;
    }

    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserInfo setLoginType(LoginTypeEnum loginType) {
        this.loginType = loginType;
        return this;
    }

    public UserInfo setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public UserInfo setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public UserInfo setPoolId(Long poolId) {
        this.poolId = poolId;
        return this;
    }

    public UserInfo setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public UserInfo setRoles(List<Long> roles) {
        this.roles = roles;
        return this;
    }
}

