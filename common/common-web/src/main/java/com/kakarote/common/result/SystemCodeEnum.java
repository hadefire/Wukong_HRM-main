/*
 * Decompiled with CFR 0.152.
 */
package com.kakarote.common.result;

import com.kakarote.common.result.ResultCode;

public enum SystemCodeEnum implements ResultCode
{
    SYSTEM_OK(0, "success"),
    SYSTEM_ERROR(500, "\u7f51\u7edc\u9519\u8bef\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5"),
    SYSTEM_NOT_LOGIN(302, "\u8bf7\u5148\u767b\u5f55\uff01"),
    SYSTEM_BAD_REQUEST(403, "\u8bf7\u6c42\u9891\u7387\u8fc7\u5feb,\u8bf7\u7a0d\u540e\u518d\u8bd5"),
    SYSTEM_NO_AUTH(401, "\u65e0\u6743\u64cd\u4f5c"),
    SYSTEM_NO_FOUND(404, "\u8d44\u6e90\u672a\u627e\u5230"),
    SYSTEM_NO_VALID(400, "\u53c2\u6570\u9a8c\u8bc1\u9519\u8bef"),
    SYSTEM_METHOD_ERROR(405, "\u8bf7\u6c42\u65b9\u5f0f\u9519\u8bef"),
    SYSTEM_REQUEST_TIMEOUT(408, "\u8bf7\u6c42\u8d85\u65f6"),
    TOKEN_VERIFICATION_EXCEPTION(410, "\u4ee4\u724c\u6821\u9a8c\u5f02\u5e38"),
    SYSTEM_USERNAME_OR_PASSWORD_ERROR(1001, "\u7528\u6237\u540d\u6216\u5bc6\u7801\u4e0d\u6b63\u786e"),
    SYSTEM_USER_DOES_NOT_EXIST(1002, "\u7528\u6237\u4e0d\u5b58\u5728"),
    SYSTEM_DATA_DOES_NOT_EXIST(1003, "\u6570\u636e\u4e0d\u5b58\u5728"),
    SYSTEM_NO_SUCH_PARAMENT_ERROR(1004, "\u53c2\u6570\u4e0d\u5b58\u5728!");

    private int code;
    private String msg;

    private SystemCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    public static SystemCodeEnum parse(Integer status) {
        for (SystemCodeEnum value : SystemCodeEnum.values()) {
            if (value.getCode() != status.intValue()) continue;
            return value;
        }
        return SYSTEM_ERROR;
    }
}

