package com.kakarote.hrm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.AdminRoleMenu;
import com.kakarote.hrm.mapper.AdminRoleMenuMapper;
import com.kakarote.hrm.service.IAdminRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单对应关系表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Service
public class AdminRoleMenuServiceImpl extends BaseServiceImpl<AdminRoleMenuMapper, AdminRoleMenu> implements IAdminRoleMenuService {

    @Override
    public List<Long> queryMenuIdsByRoleId(Long roleId) {
        return baseMapper.queryMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        // 先删除旧的关联
        baseMapper.deleteByRoleId(roleId);
        
        // 如果菜单ID列表为空，直接返回
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        
        // 批量插入新的关联
        Long currentUserId = UserUtil.getUserId();
        if (currentUserId == null) {
            currentUserId = 0L;
        }
        LocalDateTime now = LocalDateTime.now();
        
        List<AdminRoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            AdminRoleMenu roleMenu = new AdminRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateUserId(currentUserId);
            roleMenu.setUpdateUserId(currentUserId);
            roleMenu.setCreateTime(now);
            roleMenu.setUpdateTime(now);
            roleMenuList.add(roleMenu);
        }
        saveBatch(roleMenuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(Long roleId) {
        baseMapper.deleteByRoleId(roleId);
    }
}
