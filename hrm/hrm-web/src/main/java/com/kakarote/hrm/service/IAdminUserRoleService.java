package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.AdminUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色对应关系表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
public interface IAdminUserRoleService extends BaseService<AdminUserRole> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> queryRoleIdsByUserId(Long userId);

    /**
     * 根据角色类型查询拥有该类型角色的用户ID列表
     *
     * @param roleType 角色类型
     * @return 用户ID列表
     */
    List<Long> queryUserIdsByRoleType(Integer roleType);

    /**
     * 删除用户的所有角色关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(Long userId);

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void assignRoles(Long userId, List<Long> roleIds);
}
