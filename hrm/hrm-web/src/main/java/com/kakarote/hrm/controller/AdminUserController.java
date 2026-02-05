package com.kakarote.hrm.controller;

import com.kakarote.core.common.ParamAspect;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.AdminUserQueryBO;
import com.kakarote.hrm.entity.BO.AdminUserSaveBO;
import com.kakarote.hrm.entity.PO.AdminRole;
import com.kakarote.hrm.entity.VO.AdminUserVO;
import com.kakarote.hrm.service.IAdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 管理员用户管理 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/adminUser")
@Api(tags = "管理员用户管理")
@Validated
public class AdminUserController {

    @Autowired
    private IAdminUserService adminUserService;

    @PostMapping("/list")
    @ApiOperation("分页查询管理员列表")
    @ParamAspect
    public Result<BasePage<AdminUserVO>> queryList(@RequestBody AdminUserQueryBO queryBO) {
        BasePage<AdminUserVO> page = adminUserService.queryAdminUserList(queryBO);
        return Result.ok(page);
    }

    @PostMapping("/queryAll")
    @ApiOperation("查询所有管理员（不分页）")
    @ParamAspect
    public Result<List<AdminUserVO>> queryAll(
            @ApiParam(value = "角色类型（1=系统管理，3=人事）") @RequestParam(required = false) Integer roleType) {
        List<AdminUserVO> list = adminUserService.queryAllAdminUsers(roleType);
        return Result.ok(list);
    }

    @PostMapping("/queryById")
    @ApiOperation("根据员工ID查询管理员详情")
    @ParamAspect
    public Result<AdminUserVO> queryById(
            @ApiParam(value = "员工ID", required = true) @RequestParam Long employeeId) {
        AdminUserVO vo = adminUserService.queryAdminUserById(employeeId);
        return Result.ok(vo);
    }

    @PostMapping("/add")
    @ApiOperation("添加管理员（为员工分配管理角色）")
    @ParamAspect
    public Result<String> add(@Valid @RequestBody AdminUserSaveBO saveBO) {
        adminUserService.addAdminUser(saveBO);
        return Result.ok();
    }

    @PostMapping("/update")
    @ApiOperation("更新管理员角色")
    @ParamAspect
    public Result<String> update(@Valid @RequestBody AdminUserSaveBO saveBO) {
        adminUserService.updateAdminUser(saveBO);
        return Result.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("移除管理员身份")
    @ParamAspect
    public Result<String> delete(
            @ApiParam(value = "员工ID", required = true) @RequestParam Long employeeId) {
        adminUserService.removeAdminUser(employeeId);
        return Result.ok();
    }

    @PostMapping("/queryAvailableRoles")
    @ApiOperation("查询可分配的角色列表")
    @ParamAspect
    public Result<List<AdminRole>> queryAvailableRoles(
            @ApiParam(value = "角色类型（1=系统管理，3=人事）") @RequestParam(required = false) Integer roleType) {
        List<AdminRole> roles = adminUserService.queryAvailableRoles(roleType);
        return Result.ok(roles);
    }
}
