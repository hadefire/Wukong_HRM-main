package com.kakarote.hrm.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.exception.CrmException;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.entity.BO.LoginBO;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.VO.DeptVO;
import com.kakarote.hrm.entity.VO.LoginVO;
import com.kakarote.hrm.mapper.HrmEmployeeMapper;
import com.kakarote.hrm.service.IAuthService;
import com.kakarote.hrm.service.IHrmDeptService;
import com.kakarote.hrm.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private HrmEmployeeMapper employeeMapper;

    @Autowired
    private IHrmDeptService deptService;

    @Override
    public LoginVO login(LoginBO loginBO) {
        String username = loginBO.getUsername();
        String password = loginBO.getPassword();

        // 根据手机号查询员工
        LambdaQueryWrapper<HrmEmployee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrmEmployee::getMobile, username)
               .eq(HrmEmployee::getIsDel, 0);
        HrmEmployee employee = employeeMapper.selectOne(wrapper);

        if (employee == null) {
            log.warn("登录失败: 用户不存在, mobile={}", username);
            throw new CrmException(HrmCodeEnum.LOGIN_USER_NOT_FOUND);
        }

        // 检查员工状态（入职状态：1在职 2待入职 3待离职 4离职）
        if (employee.getEntryStatus() != null && employee.getEntryStatus() == 4) {
            log.warn("登录失败: 员工已离职, employeeId={}", employee.getEmployeeId());
            throw new CrmException(HrmCodeEnum.LOGIN_USER_DISABLED);
        }

        // 验证密码
        String storedPassword = employee.getPassword();
        boolean passwordMatch = false;
        
        if (StrUtil.isBlank(storedPassword)) {
            // 数据库中没有密码，只允许使用默认密码登录
            log.info("员工未设置密码，验证是否为默认密码, employeeId={}", employee.getEmployeeId());
            if (PasswordUtil.DEFAULT_PASSWORD.equals(password)) {
                // 首次登录，设置密码哈希
                String newHash = PasswordUtil.encode(password);
                com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<HrmEmployee> updateWrapper = 
                    new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
                updateWrapper.eq(HrmEmployee::getEmployeeId, employee.getEmployeeId())
                             .set(HrmEmployee::getPassword, newHash);
                employeeMapper.update(null, updateWrapper);
                log.info("首次登录，已设置密码哈希, employeeId={}", employee.getEmployeeId());
                passwordMatch = true;
            }
        } else {
            // 数据库中有密码，使用 BCrypt 验证
            passwordMatch = PasswordUtil.matches(password, storedPassword);
        }
        
        if (!passwordMatch) {
            log.warn("登录失败: 密码错误, employeeId={}", employee.getEmployeeId());
            throw new CrmException(HrmCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        // 生成 token
        String token = generateToken(employee.getEmployeeId());

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(employee.getEmployeeId());
        loginVO.setEmployeeId(employee.getEmployeeId());
        loginVO.setUsername(employee.getMobile());
        loginVO.setRealname(employee.getEmployeeName());
        loginVO.setMobile(employee.getMobile());
        loginVO.setDeptId(employee.getDeptId());
        loginVO.setPost(employee.getPost());

        // 获取部门名称
        if (employee.getDeptId() != null) {
            DeptVO dept = deptService.queryById(employee.getDeptId());
            if (dept != null) {
                loginVO.setDeptName(dept.getName());
            }
        }

        log.info("登录成功: employeeId={}, employeeName={}", employee.getEmployeeId(), employee.getEmployeeName());
        return loginVO;
    }

    @Override
    public void changePassword(Long employeeId, String oldPassword, String newPassword) {
        HrmEmployee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            throw new CrmException(HrmCodeEnum.RESULT_EMPLOYEE_NOT_EXIT);
        }

        // 验证旧密码
        String storedPassword = employee.getPassword();
        if (StrUtil.isBlank(storedPassword)) {
            storedPassword = PasswordUtil.encode(PasswordUtil.DEFAULT_PASSWORD);
        }

        if (!PasswordUtil.matches(oldPassword, storedPassword)) {
            throw new CrmException(HrmCodeEnum.LOGIN_OLD_PASSWORD_ERROR);
        }

        // 更新新密码
        LambdaUpdateWrapper<HrmEmployee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HrmEmployee::getEmployeeId, employeeId)
                     .set(HrmEmployee::getPassword, PasswordUtil.encode(newPassword));
        employeeMapper.update(null, updateWrapper);

        log.info("密码修改成功: employeeId={}", employeeId);
    }

    @Override
    public String resetPassword(Long employeeId) {
        HrmEmployee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            throw new CrmException(HrmCodeEnum.RESULT_EMPLOYEE_NOT_EXIT);
        }

        // 重置为默认密码
        String newPassword = PasswordUtil.DEFAULT_PASSWORD;
        LambdaUpdateWrapper<HrmEmployee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HrmEmployee::getEmployeeId, employeeId)
                     .set(HrmEmployee::getPassword, PasswordUtil.encode(newPassword));
        employeeMapper.update(null, updateWrapper);

        log.info("密码重置成功: employeeId={}", employeeId);
        return newPassword;
    }

    /**
     * 生成 Token
     */
    private String generateToken(Long employeeId) {
        // 简单的 token 生成：前缀 + 员工ID + 时间戳 + 随机串
        return "HRM-" + employeeId + "-" + System.currentTimeMillis() + "-" + IdUtil.fastSimpleUUID().substring(0, 8);
    }
}
