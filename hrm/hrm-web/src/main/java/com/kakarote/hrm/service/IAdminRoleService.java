package com.kakarote.hrm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.AdminRole;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminRoleService extends BaseService<AdminRole> {
    /**
     * 查询用户所属权限
     *
     * @param userId 当前用户ID
     * @return obj
     */
    JSONObject auth(Long userId);

    /**
     * 查询角色列表
     * @param roleType 角色类型，可为空
     * @return 角色列表
     */
    List<AdminRole> queryRoleList(Integer roleType);

    /**
     * 根据角色ID查询角色详情（包含菜单ID列表）
     */
    AdminRole queryRoleById(Long roleId);

    /**
     * 添加角色
     */
    Long addRole(AdminRole role);

    /**
     * 更新角色
     */
    void updateRole(AdminRole role);

    /**
     * 删除角色
     */
    void deleteRole(Long roleId);

    /**
     * 启用/禁用角色
     */
    void setRoleStatus(Long roleId, Integer status);
}


