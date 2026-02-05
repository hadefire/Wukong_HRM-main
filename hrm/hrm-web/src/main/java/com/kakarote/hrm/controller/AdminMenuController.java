package com.kakarote.hrm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.hrm.common.admin.AdminMenuVO;
import com.kakarote.hrm.common.admin.AdminRoleTypeEnum;
import com.kakarote.hrm.entity.PO.AdminMenu;
import com.kakarote.hrm.service.IAdminMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminMenu")
@Api(tags = "菜单模块")
public class AdminMenuController {

    @Autowired
    private IAdminMenuService adminMenuService;

    @RequestMapping("/getMenuListByType/{type}")
    @ApiOperation("根据类型查询菜单")
    public Result<JSONObject> getMenuListByType(@PathVariable("type") Integer type) {
        AdminRoleTypeEnum typeEnum = AdminRoleTypeEnum.parse(type);
        JSONObject byType = adminMenuService.getMenuListByType(typeEnum);
        return Result.ok(byType);
    }

    @RequestMapping("/queryAllMenuList")
    @ApiOperation("查询所有菜单列表")
    public Result<List<AdminMenu>> queryAllMenuList() {
        return Result.ok(adminMenuService.queryAllMenuList());
    }

    @PostMapping("/tree")
    @ApiOperation("查询菜单树形结构")
    public Result<List<AdminMenuVO>> queryMenuTree() {
        return Result.ok(adminMenuService.queryMenuTree());
    }

    @PostMapping("/queryById")
    @ApiOperation("查询菜单详情")
    public Result<AdminMenu> queryById(@RequestParam("menuId") Long menuId) {
        return Result.ok(adminMenuService.queryMenuById(menuId));
    }

    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public Result<Long> addMenu(@RequestBody AdminMenu menu) {
        Long menuId = adminMenuService.addMenu(menu);
        return Result.ok(menuId);
    }

    @PostMapping("/update")
    @ApiOperation("更新菜单")
    public Result<String> updateMenu(@RequestBody AdminMenu menu) {
        adminMenuService.updateMenu(menu);
        return Result.ok("更新成功");
    }

    @PostMapping("/delete")
    @ApiOperation("删除菜单")
    public Result<String> deleteMenu(@RequestParam("menuId") Long menuId) {
        adminMenuService.deleteMenu(menuId);
        return Result.ok("删除成功");
    }

    @PostMapping("/setStatus")
    @ApiOperation("启用/禁用菜单")
    public Result<String> setMenuStatus(@RequestParam("menuId") Long menuId, @RequestParam("status") Integer status) {
        adminMenuService.setMenuStatus(menuId, status);
        return Result.ok("操作成功");
    }
}

