package com.kakarote.hrm.service;

import com.kakarote.core.common.Result;

import java.util.List;

/**
 * IDS 用户服务接口（本地模式）
 */
public interface IdsUserService {

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
