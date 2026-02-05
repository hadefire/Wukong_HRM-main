package com.kakarote.hrm.service.impl;

import com.kakarote.common.entity.UserInfo;
import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.common.Result;
import com.kakarote.hrm.service.IdsUserService;
import com.kakarote.ids.provider.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 本地模式下的 IDS 用户服务实现
 * 同时实现两个接口以保证兼容性
 */
@Service
public class IdsUserServiceImpl implements IdsUserService, UserService {

    @Override
    public Result<Long> queryUserIdByMobile(String mobile) {
        UserInfo user = UserUtil.getUser();
        if (user != null && user.getUserId() != null) {
            return Result.ok(user.getUserId());
        }
        // 无上下文时返回默认用户ID
        return Result.ok(1L);
    }

    @Override
    public Result<List<Long>> queryUserByDeptIds(List<Long> deptIds) {
        // 本地模式不区分部门，返回空列表
        return Result.ok(Collections.emptyList());
    }
}
