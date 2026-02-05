package com.kakarote.hrm.controller;

import com.kakarote.common.result.Result;
import com.kakarote.core.common.ParamAspect;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地模式下的简化配置接口，避免前端 404
 */
@RestController
@RequestMapping("adminConfig")
public class AdminConfigController {

    @ParamAspect
    @PostMapping("queryAdminConfig")
    public Result<Map<String, Object>> queryAdminConfig() {
        Map<String, Object> data = new HashMap<>();
        data.put("companyName", "Ever-HRMS");
        data.put("companyLoginLogo", "");
        data.put("companyLogo", "");
        return Result.ok(data);
    }

    @ParamAspect
    @PostMapping("queryCloudConfig")
    public Result<Map<String, Object>> queryCloudConfig() {
        return Result.ok(new HashMap<>());
    }

    @ParamAspect
    @PostMapping("queryCallModuleSetting")
    public Result<Map<String, Object>> queryCallModuleSetting() {
        return Result.ok(new HashMap<>());
    }
}
