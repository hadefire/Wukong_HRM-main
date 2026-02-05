/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.kakarote.common.result;

import com.alibaba.fastjson.JSON;
import com.kakarote.common.result.ResultCode;
import com.kakarote.common.result.SystemCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(value="\u901a\u7528\u54cd\u5e94\u7ed3\u679c")
public class Result<T>
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u54cd\u5e94code,0\u4e3a\u54cd\u5e94\u6210\u529f")
    private Integer code;
    @ApiModelProperty(value="\u82e5\u54cd\u5e94code\u4e0d\u4e3a0\uff0c\u5219\u4e3a\u54cd\u5e94\u9519\u8bef\u4fe1\u606f")
    private String msg;
    @ApiModelProperty(value="\u54cd\u5e94\u6570\u636e")
    private T data;

    Result() {
    }

    private Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    private Result(ResultCode resultCode, String msg) {
        this.code = resultCode.getCode();
        this.msg = msg;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<T>(resultCode);
    }

    public static <T> Result<T> of(int code, String msg) {
        return new Result<T>(code, msg);
    }

    public static Result<String> error(ResultCode resultCode, String msg) {
        return new Result<String>(resultCode, msg);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<T>(SystemCodeEnum.SYSTEM_OK);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok() {
        return new Result<T>(SystemCodeEnum.SYSTEM_OK);
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public boolean hasSuccess() {
        return Objects.equals(SystemCodeEnum.SYSTEM_OK.getCode(), this.code);
    }

    public String toJSONString() {
        return JSON.toJSONString((Object)this);
    }

    public String toString() {
        return JSON.toJSONString((Object)this);
    }
}

