package com.kakarote.hrm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.LoginBO;
import com.kakarote.hrm.entity.VO.LoginVO;
import com.kakarote.hrm.service.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "认证管理")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<Map<String, Object>> login(@RequestBody @Validated LoginBO loginBO) {
        log.info("用户登录请求: username={}", loginBO.getUsername());
        LoginVO loginVO = authService.login(loginBO);
        
        // 返回格式兼容前端
        Map<String, Object> result = new HashMap<>();
        result.put("token", loginVO.getToken());
        result.put("userId", loginVO.getUserId());
        result.put("employeeId", loginVO.getEmployeeId());
        result.put("username", loginVO.getUsername());
        result.put("realname", loginVO.getRealname());
        result.put("mobile", loginVO.getMobile());
        result.put("deptId", loginVO.getDeptId());
        result.put("deptName", loginVO.getDeptName());
        result.put("post", loginVO.getPost());
        
        return Result.ok(result);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<String> logout() {
        // 本地模式下登出只需要前端清除 token 即可
        return Result.ok("登出成功");
    }

    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public Result<String> changePassword(@RequestParam Long employeeId,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        authService.changePassword(employeeId, oldPassword, newPassword);
        return Result.ok("密码修改成功");
    }

    @PostMapping("/resetPassword")
    @ApiOperation("重置密码（管理员操作）")
    public Result<String> resetPassword(@RequestParam Long employeeId) {
        String newPassword = authService.resetPassword(employeeId);
        return Result.ok("密码已重置为: " + newPassword);
    }
}
