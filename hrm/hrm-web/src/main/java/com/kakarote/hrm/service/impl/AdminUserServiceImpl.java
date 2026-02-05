package com.kakarote.hrm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.entity.BO.AdminUserQueryBO;
import com.kakarote.hrm.entity.BO.AdminUserSaveBO;
import com.kakarote.hrm.entity.PO.AdminRole;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.VO.AdminUserVO;
import com.kakarote.hrm.service.IAdminRoleService;
import com.kakarote.hrm.service.IAdminUserRoleService;
import com.kakarote.hrm.service.IAdminUserService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员用户 服务实现类
 * </p>
 */
@Service
public class AdminUserServiceImpl implements IAdminUserService {

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IAdminRoleService adminRoleService;

    @Autowired
    private IAdminUserRoleService adminUserRoleService;

    /**
     * 系统管理角色类型
     */
    private static final Integer ROLE_TYPE_MANAGER = 1;

    /**
     * 人事管理角色类型
     */
    private static final Integer ROLE_TYPE_HRM = 3;

    @Override
    public BasePage<AdminUserVO> queryAdminUserList(AdminUserQueryBO queryBO) {
        // 获取所有拥有管理角色的用户ID
        Set<Long> adminUserIds = new HashSet<>();

        if (queryBO.getRoleType() != null) {
            // 根据指定角色类型查询
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(queryBO.getRoleType()));
        } else {
            // 查询所有管理角色（系统管理+人事管理）
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(ROLE_TYPE_MANAGER));
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(ROLE_TYPE_HRM));
        }

        if (adminUserIds.isEmpty()) {
            return new BasePage<>();
        }

        // 查询员工信息
        LambdaQueryWrapper<HrmEmployee> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrmEmployee::getEmployeeId, adminUserIds);
        wrapper.eq(HrmEmployee::getIsDel, 0);

        // 关键字搜索
        if (StrUtil.isNotBlank(queryBO.getKeyword())) {
            wrapper.and(w -> w.like(HrmEmployee::getEmployeeName, queryBO.getKeyword())
                    .or().like(HrmEmployee::getMobile, queryBO.getKeyword()));
        }

        // 部门筛选
        if (queryBO.getDeptId() != null) {
            wrapper.eq(HrmEmployee::getDeptId, queryBO.getDeptId());
        }

        // 分页查询
        BasePage<HrmEmployee> employeePage = employeeService.page(
                new BasePage<>(queryBO.getPage(), queryBO.getLimit()), wrapper);

        // 转换为 VO
        BasePage<AdminUserVO> resultPage = new BasePage<>();
        resultPage.setSize(employeePage.getSize());
        resultPage.setCurrent(employeePage.getCurrent());
        resultPage.setTotal(employeePage.getTotal());
        resultPage.setPages(employeePage.getPages());
        resultPage.setList(convertToVOList(employeePage.getList()));

        return resultPage;
    }

    @Override
    public List<AdminUserVO> queryAllAdminUsers(Integer roleType) {
        Set<Long> adminUserIds = new HashSet<>();

        if (roleType != null) {
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(roleType));
        } else {
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(ROLE_TYPE_MANAGER));
            adminUserIds.addAll(adminUserRoleService.queryUserIdsByRoleType(ROLE_TYPE_HRM));
        }

        if (adminUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<HrmEmployee> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrmEmployee::getEmployeeId, adminUserIds);
        wrapper.eq(HrmEmployee::getIsDel, 0);
        List<HrmEmployee> employees = employeeService.list(wrapper);

        return convertToVOList(employees);
    }

    @Override
    public AdminUserVO queryAdminUserById(Long employeeId) {
        HrmEmployee employee = employeeService.getById(employeeId);
        if (employee == null || employee.getIsDel() == 1) {
            return null;
        }
        return convertToVO(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAdminUser(AdminUserSaveBO saveBO) {
        // 验证员工存在
        HrmEmployee employee = employeeService.getById(saveBO.getEmployeeId());
        if (employee == null || employee.getIsDel() == 1) {
            throw new CrmException(HrmCodeEnum.RESULT_EMPLOYEE_NOT_EXIT);
        }

        // 验证角色存在且是管理角色
        validateRoles(saveBO.getRoleIds());

        // 分配角色
        adminUserRoleService.assignRoles(saveBO.getEmployeeId(), saveBO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminUser(AdminUserSaveBO saveBO) {
        // 验证员工存在
        HrmEmployee employee = employeeService.getById(saveBO.getEmployeeId());
        if (employee == null || employee.getIsDel() == 1) {
            throw new CrmException(HrmCodeEnum.RESULT_EMPLOYEE_NOT_EXIT);
        }

        // 验证角色存在且是管理角色
        validateRoles(saveBO.getRoleIds());

        // 更新角色（先删后增）
        adminUserRoleService.assignRoles(saveBO.getEmployeeId(), saveBO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAdminUser(Long employeeId) {
        // 删除用户的所有管理角色关联
        adminUserRoleService.deleteByUserId(employeeId);
    }

    @Override
    public List<AdminRole> queryAvailableRoles(Integer roleType) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getStatus, 1); // 只查询启用的角色

        if (roleType != null) {
            wrapper.eq(AdminRole::getRoleType, roleType);
        } else {
            // 查询管理类角色（系统管理+人事管理）
            wrapper.in(AdminRole::getRoleType, Arrays.asList(ROLE_TYPE_MANAGER, ROLE_TYPE_HRM));
        }

        wrapper.orderByAsc(AdminRole::getSorting);
        return adminRoleService.list(wrapper);
    }

    /**
     * 验证角色是否有效
     */
    private void validateRoles(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            throw new CrmException(HrmCodeEnum.RESULT_ROLE_NOT_EXIT);
        }

        List<AdminRole> roles = adminRoleService.listByIds(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new CrmException(HrmCodeEnum.RESULT_ROLE_NOT_EXIT);
        }

        // 验证是否都是管理类角色
        for (AdminRole role : roles) {
            if (role.getRoleType() != ROLE_TYPE_MANAGER && role.getRoleType() != ROLE_TYPE_HRM) {
                throw new CrmException(HrmCodeEnum.RESULT_ROLE_NOT_EXIT);
            }
        }
    }

    /**
     * 批量转换为 VO
     */
    private List<AdminUserVO> convertToVOList(List<HrmEmployee> employees) {
        if (CollUtil.isEmpty(employees)) {
            return Collections.emptyList();
        }
        return employees.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 转换为 VO
     */
    private AdminUserVO convertToVO(HrmEmployee employee) {
        AdminUserVO vo = new AdminUserVO();
        vo.setEmployeeId(employee.getEmployeeId());
        vo.setEmployeeName(employee.getEmployeeName());
        vo.setMobile(employee.getMobile());
        vo.setEmail(employee.getEmail());
        vo.setDeptId(employee.getDeptId());
        vo.setDeptName(employee.getDeptName());
        vo.setPost(employee.getPost());
        vo.setCreateTime(employee.getCreateTime());

        // 查询用户角色
        List<Long> roleIds = adminUserRoleService.queryRoleIdsByUserId(employee.getEmployeeId());
        if (CollUtil.isNotEmpty(roleIds)) {
            List<AdminRole> roles = adminRoleService.listByIds(roleIds);
            List<AdminUserVO.AdminRoleVO> roleVOs = roles.stream().map(role -> {
                AdminUserVO.AdminRoleVO roleVO = new AdminUserVO.AdminRoleVO();
                roleVO.setRoleId(role.getRoleId());
                roleVO.setRoleName(role.getRoleName());
                roleVO.setRoleType(role.getRoleType());
                roleVO.setDataType(role.getDataType());
                return roleVO;
            }).collect(Collectors.toList());
            vo.setRoles(roleVOs);

            // 判断是否超级管理员和HRM管理员
            vo.setIsSuperAdmin(roles.stream().anyMatch(r -> r.getRoleType() == ROLE_TYPE_MANAGER));
            vo.setIsHrmAdmin(roles.stream().anyMatch(r -> r.getRoleType() == ROLE_TYPE_HRM));
        } else {
            vo.setRoles(Collections.emptyList());
            vo.setIsSuperAdmin(false);
            vo.setIsHrmAdmin(false);
        }

        return vo;
    }
}
