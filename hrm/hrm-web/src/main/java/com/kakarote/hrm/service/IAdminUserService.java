package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.AdminUserQueryBO;
import com.kakarote.hrm.entity.BO.AdminUserSaveBO;
import com.kakarote.hrm.entity.PO.AdminRole;
import com.kakarote.hrm.entity.VO.AdminUserVO;

import java.util.List;

/**
 * <p>
 * 管理员用户 服务类
 * </p>
 */
public interface IAdminUserService {

    /**
     * 分页查询管理员列表
     *
     * @param queryBO 查询条件
     * @return 管理员列表
     */
    BasePage<AdminUserVO> queryAdminUserList(AdminUserQueryBO queryBO);

    /**
     * 查询所有管理员列表（不分页）
     *
     * @param roleType 角色类型（可选）
     * @return 管理员列表
     */
    List<AdminUserVO> queryAllAdminUsers(Integer roleType);

    /**
     * 根据员工ID查询管理员信息
     *
     * @param employeeId 员工ID
     * @return 管理员信息
     */
    AdminUserVO queryAdminUserById(Long employeeId);

    /**
     * 添加管理员（为员工分配管理角色）
     *
     * @param saveBO 保存参数
     */
    void addAdminUser(AdminUserSaveBO saveBO);

    /**
     * 更新管理员角色
     *
     * @param saveBO 保存参数
     */
    void updateAdminUser(AdminUserSaveBO saveBO);

    /**
     * 移除管理员身份（删除用户的所有管理角色）
     *
     * @param employeeId 员工ID
     */
    void removeAdminUser(Long employeeId);

    /**
     * 查询可分配的角色列表
     *
     * @param roleType 角色类型（可选，1=系统管理，3=人事）
     * @return 角色列表
     */
    List<AdminRole> queryAvailableRoles(Integer roleType);
}
