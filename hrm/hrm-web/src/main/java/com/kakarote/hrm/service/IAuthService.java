package com.kakarote.hrm.service;

import com.kakarote.hrm.entity.BO.LoginBO;
import com.kakarote.hrm.entity.VO.LoginVO;

/**
 * 认证服务接口
 */
public interface IAuthService {

    /**
     * 用户登录
     *
     * @param loginBO 登录参数
     * @return 登录结果
     */
    LoginVO login(LoginBO loginBO);

    /**
     * 修改密码
     *
     * @param employeeId  员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long employeeId, String oldPassword, String newPassword);

    /**
     * 重置密码（管理员操作）
     *
     * @param employeeId 员工ID
     * @return 新密码（明文，用于通知用户）
     */
    String resetPassword(Long employeeId);
}
