package com.kakarote.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.common.entity.UserInfo;
import com.kakarote.common.utils.UserUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.exception.NoLoginException;
import com.kakarote.core.redis.Redis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangzhiwei
 * user注入切面
 */
@Aspect
@Component
@ConditionalOnClass(value = {ServletRequest.class, HandlerMapping.class})
public class ParamAspect implements Ordered {
    @Autowired
    private Redis redis;

    @Value("${wukong.auth.local.enabled:false}")
    private boolean localAuthEnabled;

    @Value("${wukong.auth.local.user-id:1}")
    private Long localUserId;

    @Value("${wukong.auth.local.dept-id:1}")
    private Long localDeptId;

    @Value("${wukong.auth.local.company-id:1}")
    private Long localCompanyId;

    @Value("${wukong.auth.local.username:local-admin}")
    private String localUsername;

    @Value("${wukong.auth.local.realname:local-admin}")
    private String localRealname;

    @Value("${wukong.auth.local.nickname:local-admin}")
    private String localNickname;

    @Value("${wukong.auth.local.mobile:13800000000}")
    private String localMobile;

    @Value("${wukong.auth.local.admin:true}")
    private boolean localIsAdmin;

    @Around("(execution(* com.kakarote.*.controller..*(..))||execution(* com.kakarote.*.*.controller..*(..))) && execution(@(org.springframework.web.bind.annotation.*Mapping) * *(..))  && !execution(@(com.kakarote.core.common.ParamAspect) * *(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("[ParamAspect] >>> Entering around() for: " + point.getSignature().toShortString());
        System.out.println("[ParamAspect] localAuthEnabled = " + localAuthEnabled);
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        try {
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader(Const.DEFAULT_TOKEN_NAME);
                System.out.println("[ParamAspect] token from header = " + token);
                UserInfo info = null;
                if (StrUtil.isNotEmpty(token) && redis.exists(token + Const.TOKEN_CACHE_NAME)) {
                    info = redis.get(token);
                    System.out.println("[ParamAspect] Got user from Redis: " + (info != null));
                }
                if (ObjectUtil.isNull(info) && localAuthEnabled) {
                    System.out.println("[ParamAspect] Building local user...");
                    info = buildLocalUser();
                    System.out.println("[ParamAspect] Local user built, mobile = " + (info != null ? info.getMobile() : "null"));
                }
                if (ObjectUtil.isNull(info)) {
                    System.out.println("[ParamAspect] No user info, throwing NoLoginException");
                    throw new NoLoginException();
                }
                UserUtil.setUser(info);
                System.out.println("[ParamAspect] User set in UserUtil");
            } else {
                System.out.println("[ParamAspect] WARNING: attributes is null!");
            }
            return point.proceed();
        } finally {
           UserUtil.removeUser();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private UserInfo buildLocalUser() {
        UserInfo info = new UserInfo();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(info);
        setIfWritable(wrapper, "userId", localUserId);
        setIfWritable(wrapper, "deptId", localDeptId);
        setIfWritable(wrapper, "companyId", localCompanyId);
        setIfWritable(wrapper, "username", localUsername);
        setIfWritable(wrapper, "realname", localRealname);
        setIfWritable(wrapper, "nickname", localNickname);
        setIfWritable(wrapper, "mobile", localMobile);
        setIfWritable(wrapper, "isAdmin", localIsAdmin);
        setIfWritable(wrapper, "admin", localIsAdmin);
        setIfWritable(wrapper, "roleType", localIsAdmin ? 1 : 2);
        return info;
    }

    private void setIfWritable(BeanWrapperImpl wrapper, String property, Object value) {
        if (property == null || value == null) {
            return;
        }
        if (wrapper.isWritableProperty(property)) {
            wrapper.setPropertyValue(property, value);
        }
    }
}