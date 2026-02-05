package com.kakarote.hrm.service.impl;

import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.AdminUserRole;
import com.kakarote.hrm.mapper.AdminUserRoleMapper;
import com.kakarote.hrm.service.IAdminUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色对应关系表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Service
public class AdminUserRoleServiceImpl extends BaseServiceImpl<AdminUserRoleMapper, AdminUserRole> implements IAdminUserRoleService {

    @Override
    public List<Long> queryRoleIdsByUserId(Long userId) {
        return getBaseMapper().queryRoleIdsByUserId(userId);
    }

    @Override
    public List<Long> queryUserIdsByRoleType(Integer roleType) {
        return getBaseMapper().queryUserIdsByRoleType(roleType);
    }

    @Override
    public int deleteByUserId(Long userId) {
        return getBaseMapper().deleteByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) {
        // 先删除用户现有的所有角色关联
        deleteByUserId(userId);

        // 批量插入新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<AdminUserRole> userRoles = new ArrayList<>();
            // 获取当前用户ID，如果为空则使用目标用户ID作为创建者
            Long currentUserId = UserUtil.getUserId();
            if (currentUserId == null) {
                currentUserId = userId;
            }

            for (Long roleId : roleIds) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateUserId(currentUserId);
                userRole.setUpdateUserId(currentUserId);
                userRole.setCreateTime(java.time.LocalDateTime.now());
                userRole.setUpdateTime(java.time.LocalDateTime.now());
                userRoles.add(userRole);
            }

            saveBatch(userRoles);
        }
    }
}
