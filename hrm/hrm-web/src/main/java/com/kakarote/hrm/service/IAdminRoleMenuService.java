package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.AdminRoleMenu;

import java.util.List;

/**
 * <p>
 * 角色菜单对应关系表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
public interface IAdminRoleMenuService extends BaseService<AdminRoleMenu> {

    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> queryMenuIdsByRoleId(Long roleId);

    /**
     * 保存角色菜单关联（先删除旧的，再插入新的）
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 根据角色ID删除角色菜单关联
     */
    void deleteByRoleId(Long roleId);
}
