package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.AdminRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色菜单对应关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
public interface AdminRoleMenuMapper extends BaseMapper<AdminRoleMenu> {

    /**
     * 根据角色ID查询菜单ID列表
     */
    @Select("SELECT menu_id FROM wk_admin_role_menu WHERE role_id = #{roleId}")
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID删除角色菜单关联
     */
    @Delete("DELETE FROM wk_admin_role_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);
}
