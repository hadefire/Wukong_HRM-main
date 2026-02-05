package com.kakarote.ids.provider.service;

import com.kakarote.core.common.Result;

import java.util.List;

/**
 * 本地简化版用户服务接口
 * 注意：本地模式下不使用 FeignClient，由 LocalUserServiceImpl 提供实现
 */
public interface UserService {

    /**
     * 根据手机号查询用户ID
     *
     * @param mobile 手机号
     * @return 用户ID
     */
    Result<Long> queryUserIdByMobile(String mobile);

    /**
     * 根据部门ID列表查询用户ID列表
     *
     * @param deptIds 部门ID列表
     * @return 用户ID列表
     */
    Result<List<Long>> queryUserByDeptIds(List<Long> deptIds);
}
