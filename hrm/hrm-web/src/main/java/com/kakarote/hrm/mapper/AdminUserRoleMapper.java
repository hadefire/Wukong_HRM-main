package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.AdminUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色对应关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> queryRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色类型查询拥有该类型角色的用户ID列表
     *
     * @param roleType 角色类型
     * @return 用户ID列表
     */
    List<Long> queryUserIdsByRoleType(@Param("roleType") Integer roleType);

    /**
     * 删除用户的所有角色关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(@Param("userId") Long userId);
}
