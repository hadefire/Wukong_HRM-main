package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.common.admin.AdminMenuVO;
import com.kakarote.hrm.common.admin.AdminRoleTypeEnum;
import com.kakarote.hrm.entity.PO.AdminMenu;
import com.kakarote.hrm.entity.PO.AdminRoleMenu;
import com.kakarote.hrm.mapper.AdminMenuMapper;
import com.kakarote.hrm.service.IAdminMenuService;
import com.kakarote.hrm.service.IAdminRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminMenuServiceImpl extends BaseServiceImpl<AdminMenuMapper, AdminMenu> implements IAdminMenuService {

    @Autowired
    private IAdminRoleMenuService adminRoleMenuService;

    /**
     * 查询用户所拥有的菜单权限
     *
     * @param userId 用户列表
     * @return 菜单权限的并集
     */
    @Override
    public List<AdminMenu> queryMenuList(Long userId) {
        return query().list();
    }

    /**
     * 根据类型查询菜单
     *
     * @param typeEnum type
     * @return data
     */
    @Override
    public JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum) {
        JSONObject object = new JSONObject();
        String realm = typeEnum.getName();
        AdminMenuVO data = queryMenuListByRealm(realm);
        if (data != null && data.getMenuId() != null) {
            List<AdminMenuVO> menuList = getMenuList(data.getMenuId());
            data.setChildMenu(menuList);
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("menuName_resourceKey", "promission.system." + data.getMenuId());
            data.setLanguageKeyMap(keyMap);
        }
        object.put("data", data);
        return object;
    }

    /**
     * 通过parentId和realm查询菜单
     *
     * @return 菜单列表
     */
    private AdminMenuVO queryMenuListByRealm(String realm) {
        AdminMenu adminMenu = lambdaQuery().eq(AdminMenu::getParentId, 0).eq(AdminMenu::getRealm, realm).one();
        return BeanUtil.copyProperties(adminMenu, AdminMenuVO.class);
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    @Override
    public List<AdminMenu> queryAllMenuList() {
        return lambdaQuery().orderByAsc(AdminMenu::getSort).list();
    }

    @Override
    public List<AdminMenuVO> queryMenuTree() {
        List<AdminMenu> allMenus = lambdaQuery().orderByAsc(AdminMenu::getSort).list();
        if (CollUtil.isEmpty(allMenus)) {
            return new ArrayList<>();
        }
        // 添加语言包信息
        for (AdminMenu adminMenu : allMenus) {
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("menuName_resourceKey", "promission.system." + adminMenu.getMenuId());
            adminMenu.setLanguageKeyMap(keyMap);
        }
        return RecursionUtil.getChildListTree(allMenus, "parentId", 0L, "menuId", "childMenu", AdminMenuVO.class);
    }

    @Override
    public AdminMenu queryMenuById(Long menuId) {
        return getById(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addMenu(AdminMenu menu) {
        // 检查同级下菜单名称是否重复
        LambdaQueryWrapper<AdminMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminMenu::getMenuName, menu.getMenuName())
               .eq(AdminMenu::getParentId, menu.getParentId() != null ? menu.getParentId() : 0L);
        if (count(wrapper) > 0) {
            throw new CrmException(HrmCodeEnum.MENU_NAME_EXISTS);
        }
        
        // 设置默认值
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getSort() == null) {
            menu.setSort(0);
        }
        if (menu.getMenuType() == null) {
            menu.setMenuType(2); // 默认为菜单类型
        }
        
        save(menu);
        return menu.getMenuId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(AdminMenu menu) {
        if (menu.getMenuId() == null) {
            throw new CrmException(HrmCodeEnum.MENU_NOT_FOUND);
        }
        
        // 检查菜单是否存在
        AdminMenu existMenu = getById(menu.getMenuId());
        if (existMenu == null) {
            throw new CrmException(HrmCodeEnum.MENU_NOT_FOUND);
        }
        
        // 检查同级下菜单名称是否重复（排除自己）
        if (menu.getMenuName() != null) {
            Long parentId = menu.getParentId() != null ? menu.getParentId() : existMenu.getParentId();
            LambdaQueryWrapper<AdminMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AdminMenu::getMenuName, menu.getMenuName())
                   .eq(AdminMenu::getParentId, parentId)
                   .ne(AdminMenu::getMenuId, menu.getMenuId());
            if (count(wrapper) > 0) {
                throw new CrmException(HrmCodeEnum.MENU_NAME_EXISTS);
            }
        }
        
        updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {
        // 检查菜单是否存在
        AdminMenu menu = getById(menuId);
        if (menu == null) {
            throw new CrmException(HrmCodeEnum.MENU_NOT_FOUND);
        }
        
        // 检查是否有子菜单
        LambdaQueryWrapper<AdminMenu> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(AdminMenu::getParentId, menuId);
        if (count(childWrapper) > 0) {
            throw new CrmException(HrmCodeEnum.MENU_HAS_CHILDREN);
        }
        
        // 检查是否被角色使用
        LambdaQueryWrapper<AdminRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(AdminRoleMenu::getMenuId, menuId);
        if (adminRoleMenuService.count(roleMenuWrapper) > 0) {
            throw new CrmException(HrmCodeEnum.MENU_HAS_ROLES);
        }
        
        // 删除菜单
        removeById(menuId);
    }

    @Override
    public void setMenuStatus(Long menuId, Integer status) {
        AdminMenu menu = new AdminMenu();
        menu.setMenuId(menuId);
        menu.setStatus(status);
        updateById(menu);
    }

    private List<AdminMenuVO> getMenuList(Long parentId, String... notRealm) {
        LambdaQueryChainWrapper<AdminMenu> chainWrapper = lambdaQuery();
        if (notRealm.length > 0) {
            chainWrapper.notIn(AdminMenu::getRealm, Arrays.asList(notRealm));
        }
        chainWrapper.orderByAsc(AdminMenu::getSort);
        List<AdminMenu> list = chainWrapper.list();
        if (CollectionUtil.isNotEmpty(list)) {
            for (AdminMenu adminMenu : list) {
                Map<String, String> keyMap = new HashMap<>();
                keyMap.put("menuName_resourceKey", "promission.system." + adminMenu.getMenuId());
                adminMenu.setLanguageKeyMap(keyMap);
            }
        }
        return RecursionUtil.getChildListTree(list, "parentId", parentId, "menuId", "childMenu", AdminMenuVO.class);
    }
}
