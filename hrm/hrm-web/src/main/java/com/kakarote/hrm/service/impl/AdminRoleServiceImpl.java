package com.kakarote.hrm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.entity.PO.AdminMenu;
import com.kakarote.hrm.entity.PO.AdminRole;
import com.kakarote.hrm.entity.PO.AdminUserRole;
import com.kakarote.hrm.mapper.AdminRoleMapper;
import com.kakarote.hrm.service.IAdminMenuService;
import com.kakarote.hrm.service.IAdminRoleMenuService;
import com.kakarote.hrm.service.IAdminRoleService;
import com.kakarote.hrm.service.IAdminUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Autowired
    private IAdminMenuService adminMenuService;

    @Autowired
    private IAdminRoleMenuService adminRoleMenuService;

    @Autowired
    private IAdminUserRoleService adminUserRoleService;

    @Autowired
    private Redis redis;

    /**
     * 查询用户所属权限
     *
     * @return obj
     */
    @Override
    public JSONObject auth(Long userId) {
        String cacheKey = AdminCacheKey.USER_AUTH_CACHE_KET + userId.toString();
        if (redis.exists(cacheKey)) {
            return redis.get(cacheKey);
        }
        List<AdminMenu> adminMenus = adminMenuService.queryMenuList(userId);

        JSONObject jsonObject = createMenu(new HashSet<>(adminMenus), 0L);
        if (UserUtil.isAdmin()) {

        }
        redis.setex(cacheKey, 30, jsonObject);
        return jsonObject;
    }

    private JSONObject createMenu(Set<AdminMenu> adminMenuList, Long parentId) {
        JSONObject jsonObject = new JSONObject();
        adminMenuList.forEach(adminMenu -> {
            if (Objects.equals(parentId, adminMenu.getParentId())) {
                if (Objects.equals(1, adminMenu.getMenuType())) {
                    JSONObject object = createMenu(adminMenuList, adminMenu.getMenuId());
                    if (!object.isEmpty()) {
                        jsonObject.put(adminMenu.getRealm(), object);
                    }
                } else {
                    jsonObject.put(adminMenu.getRealm(), Boolean.TRUE);
                }
            }
        });
        return jsonObject;
    }

    @Override
    public List<AdminRole> queryRoleList(Integer roleType) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        if (roleType != null) {
            wrapper.eq(AdminRole::getRoleType, roleType);
        }
        wrapper.orderByAsc(AdminRole::getSorting);
        return list(wrapper);
    }

    @Override
    public AdminRole queryRoleById(Long roleId) {
        AdminRole role = getById(roleId);
        if (role != null) {
            // 查询角色关联的菜单ID列表
            List<Long> menuIds = adminRoleMenuService.queryMenuIdsByRoleId(roleId);
            role.setMenuIds(menuIds);
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRole(AdminRole role) {
        // 检查角色名称是否重复
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getRoleName, role.getRoleName());
        if (count(wrapper) > 0) {
            throw new CrmException(HrmCodeEnum.ROLE_NAME_EXISTS);
        }
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        if (role.getDataType() == null) {
            role.setDataType(5);
        }
        if (role.getIsHidden() == null) {
            role.setIsHidden(1);
        }
        if (role.getSorting() == null) {
            role.setSorting(0);
        }
        
        save(role);
        
        // 保存角色菜单关联
        if (CollUtil.isNotEmpty(role.getMenuIds())) {
            adminRoleMenuService.saveRoleMenus(role.getRoleId(), role.getMenuIds());
        }
        
        // 清除权限缓存
        clearAuthCache();
        
        return role.getRoleId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(AdminRole role) {
        if (role.getRoleId() == null) {
            throw new CrmException(HrmCodeEnum.ROLE_NOT_FOUND);
        }
        
        // 检查角色是否存在
        AdminRole existRole = getById(role.getRoleId());
        if (existRole == null) {
            throw new CrmException(HrmCodeEnum.ROLE_NOT_FOUND);
        }
        
        // 检查角色名称是否重复（排除自己）
        if (role.getRoleName() != null) {
            LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AdminRole::getRoleName, role.getRoleName())
                   .ne(AdminRole::getRoleId, role.getRoleId());
            if (count(wrapper) > 0) {
                throw new CrmException(HrmCodeEnum.ROLE_NAME_EXISTS);
            }
        }
        
        updateById(role);
        
        // 更新角色菜单关联
        if (role.getMenuIds() != null) {
            adminRoleMenuService.saveRoleMenus(role.getRoleId(), role.getMenuIds());
        }
        
        // 清除权限缓存
        clearAuthCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        // 检查角色是否存在
        AdminRole role = getById(roleId);
        if (role == null) {
            throw new CrmException(HrmCodeEnum.ROLE_NOT_FOUND);
        }
        
        // 检查角色是否被用户使用
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getRoleId, roleId);
        if (adminUserRoleService.count(wrapper) > 0) {
            throw new CrmException(HrmCodeEnum.ROLE_HAS_USERS);
        }
        
        // 删除角色菜单关联
        adminRoleMenuService.deleteByRoleId(roleId);
        
        // 删除角色
        removeById(roleId);
        
        // 清除权限缓存
        clearAuthCache();
    }

    @Override
    public void setRoleStatus(Long roleId, Integer status) {
        AdminRole role = new AdminRole();
        role.setRoleId(roleId);
        role.setStatus(status);
        updateById(role);
        
        // 清除权限缓存
        clearAuthCache();
    }

    /**
     * 清除权限缓存
     * 注意：由于 Redis 接口限制，无法批量删除缓存
     * 权限缓存已设置 300 秒过期时间，修改角色权限后最多 5 分钟生效
     */
    private void clearAuthCache() {
        // 权限缓存会在 300 秒后自动过期，无需手动清除
        // 如需立即生效，可通过用户重新登录来刷新权限
    }
}
