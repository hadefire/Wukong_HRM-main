package com.kakarote.hrm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.common.admin.AdminMenuVO;
import com.kakarote.hrm.common.admin.AdminRoleTypeEnum;
import com.kakarote.hrm.entity.PO.AdminMenu;

import java.util.List;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminMenuService extends BaseService<AdminMenu> {
    /**
     * 查询用户所拥有的菜单权限
     *
     * @param userId    用户列表
     * @return 菜单权限的并集
     */
    List<AdminMenu> queryMenuList(Long userId);

    /**
     * 根据类型查询菜单
     *
     * @param typeEnum type
     * @return data
     */
    JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum);

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    List<AdminMenu> queryAllMenuList();

    /**
     * 查询菜单树形结构
     */
    List<AdminMenuVO> queryMenuTree();

    /**
     * 根据菜单ID查询菜单详情
     */
    AdminMenu queryMenuById(Long menuId);

    /**
     * 添加菜单
     */
    Long addMenu(AdminMenu menu);

    /**
     * 更新菜单
     */
    void updateMenu(AdminMenu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Long menuId);

    /**
     * 启用/禁用菜单
     */
    void setMenuStatus(Long menuId, Integer status);
}
