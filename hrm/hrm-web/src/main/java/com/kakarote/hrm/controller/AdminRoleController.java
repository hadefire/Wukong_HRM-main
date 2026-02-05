package com.kakarote.hrm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.PO.AdminRole;
import com.kakarote.hrm.service.IAdminRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminRole")
@Api(tags = "角色模块")
public class AdminRoleController {
    @Autowired
    private IAdminRoleService adminRoleService;


    @PostMapping("/auth")
    @ApiOperation("角色权限")
    public Result<JSONObject> auth() {
        JSONObject object = adminRoleService.auth(UserUtil.getUserId());
        return Result.ok(object);
    }

    @PostMapping(value = "/queryRoleByRoleId")
    @ApiExplain("查询角色信息")
    public Result<AdminRole> queryRoleByRoleId(@RequestParam("roleId") Long roleId) {
        return Result.ok(adminRoleService.getById(roleId));
    }

    @PostMapping("/list")
    @ApiOperation("查询角色列表")
    public Result<List<AdminRole>> queryRoleList(@RequestParam(value = "roleType", required = false) Integer roleType) {
        return Result.ok(adminRoleService.queryRoleList(roleType));
    }

    @PostMapping("/queryById")
    @ApiOperation("查询角色详情（包含菜单权限）")
    public Result<AdminRole> queryById(@RequestParam("roleId") Long roleId) {
        return Result.ok(adminRoleService.queryRoleById(roleId));
    }

    @PostMapping("/add")
    @ApiOperation("添加角色")
    public Result<Long> addRole(@RequestBody AdminRole role) {
        Long roleId = adminRoleService.addRole(role);
        return Result.ok(roleId);
    }

    @PostMapping("/update")
    @ApiOperation("更新角色")
    public Result<String> updateRole(@RequestBody AdminRole role) {
        adminRoleService.updateRole(role);
        return Result.ok("更新成功");
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public Result<String> deleteRole(@RequestParam("roleId") Long roleId) {
        adminRoleService.deleteRole(roleId);
        return Result.ok("删除成功");
    }

    @PostMapping("/setStatus")
    @ApiOperation("启用/禁用角色")
    public Result<String> setRoleStatus(@RequestParam("roleId") Long roleId, @RequestParam("status") Integer status) {
        adminRoleService.setRoleStatus(roleId, status);
        return Result.ok("操作成功");
    }
}

