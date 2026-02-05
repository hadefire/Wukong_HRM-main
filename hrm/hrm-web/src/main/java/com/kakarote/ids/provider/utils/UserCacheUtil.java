package com.kakarote.ids.provider.utils;

import com.kakarote.common.entity.SimpleUser;
import com.kakarote.common.entity.UserInfo;
import com.kakarote.common.utils.UserUtil;

/**
 * 本地简化版用户缓存工具
 */
public class UserCacheUtil {

    public static SimpleUser getSimpleUser(Long userId) {
        SimpleUser user = new SimpleUser();
        user.setUserId(userId);
        UserInfo current = UserUtil.getUser();
        if (current != null) {
            user.setUsername(current.getUsername());
            user.setNickname(current.getNickname());
            user.setUserImg(current.getUserImg());
            user.setDeptId(current.getDeptId());
        } else {
            user.setUsername("local-user");
            user.setNickname("local-user");
        }
        return user;
    }

    public static String getUserName(Long userId) {
        SimpleUser user = getSimpleUser(userId);
        if (user.getNickname() != null && !user.getNickname().isEmpty()) {
            return user.getNickname();
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername();
        }
        return "local-user";
    }

    public static String getDeptName(Long deptId) {
        if (deptId == null) {
            return "";
        }
        return "dept-" + deptId;
    }
}
