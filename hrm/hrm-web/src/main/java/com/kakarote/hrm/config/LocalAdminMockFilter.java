package com.kakarote.hrm.config;

import com.alibaba.fastjson.JSON;
import com.kakarote.core.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@ConditionalOnProperty(prefix = "wukong.auth.local", name = "enabled", havingValue = "true")
public class LocalAdminMockFilter extends OncePerRequestFilter {

    @Value("${wukong.auth.local.token:LOCAL-AUTH-TOKEN}")
    private String localToken;

    @Value("${wukong.auth.local.user-id:1}")
    private Long userId;

    @Value("${wukong.auth.local.dept-id:1}")
    private Long deptId;

    @Value("${wukong.auth.local.company-id:1}")
    private Long companyId;

    @Value("${wukong.auth.local.username:local-admin}")
    private String username;

    @Value("${wukong.auth.local.realname:local-admin}")
    private String realname;

    @Value("${wukong.auth.local.nickname:local-admin}")
    private String nickname;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Result<?> result = buildMockResult(path);
        if (result != null) {
            writeJson(response, result);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Result<?> buildMockResult(String path) {
        // /auth/login 走真正的登录控制器，不再拦截
        if (path.endsWith("/auth/login") || path.endsWith("/auth/logout")) {
            return null;
        }
        // 旧的 /login 接口保持兼容（如果有其他地方还在用）
        if (path.endsWith("/login") && !path.contains("/auth/")) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", localToken);
            return Result.ok(data);
        }
        if (path.endsWith("/adminUser/authorization")) {
            return Result.ok(localToken);
        }
        if (path.endsWith("/adminUser/logout")) {
            return Result.ok();
        }
        if (path.endsWith("/adminUser/queryLoginUser") || path.endsWith("/adminUser/queryUserInfo")) {
            return Result.ok(buildUserInfo());
        }
        if (path.endsWith("/adminUser/queryUserList")) {
            Map<String, Object> data = new HashMap<>();
            data.put("list", Collections.singletonList(buildSimpleUser()));
            data.put("totalRow", 1);
            data.put("total", 1);
            return Result.ok(data);
        }
        if (path.endsWith("/adminUser/queryDeptTree")) {
            return Result.ok(Collections.singletonList(buildDept()));
        }
        if (path.endsWith("/adminUser/queryOrganizationInfo")) {
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> deptList = Collections.singletonList(buildDept());
            Map<String, List<Map<String, Object>>> userMap = new HashMap<>();
            userMap.put(String.valueOf(deptId), Collections.singletonList(buildSimpleUser()));

            data.put("deptList", deptList);
            data.put("userMap", userMap);
            data.put("disableUserList", Collections.emptyList());
            return Result.ok(data);
        }
        if (path.endsWith("/adminUser/queryUserDeptOrRoleInfo")) {
            Map<String, Object> data = new HashMap<>();
            data.put("userList", Collections.singletonList(buildSimpleUser()));
            data.put("deptList", Collections.singletonList(buildDept()));
            data.put("roleList", Collections.emptyList());
            return Result.ok(data);
        }

        return null;
    }

    private Map<String, Object> buildDept() {
        Map<String, Object> dept = new HashMap<>();
        dept.put("deptId", deptId);
        dept.put("name", "默认部门");
        dept.put("children", Collections.emptyList());
        return dept;
    }

    private Map<String, Object> buildSimpleUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("realname", realname);
        user.put("nickname", nickname);
        user.put("deptId", deptId);
        user.put("username", username);
        return user;
    }

    private Map<String, Object> buildUserInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("userId", userId);
        info.put("realname", realname);
        info.put("nickname", nickname);
        info.put("deptId", deptId);
        info.put("companyId", companyId);
        info.put("username", username);
        info.put("loginType", 1);
        return info;
    }

    private void writeJson(HttpServletResponse response, Result<?> result) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
