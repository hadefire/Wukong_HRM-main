/*
 * Decompiled with CFR 0.152.
 */
package com.kakarote.common.exception;

import com.kakarote.common.result.ResultCode;

public class BusinessException
extends RuntimeException
implements ResultCode {
    private final Integer code;
    private final String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }
}

