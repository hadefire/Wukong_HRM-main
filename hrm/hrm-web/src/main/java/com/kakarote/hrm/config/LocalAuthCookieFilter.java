package com.kakarote.hrm.config;

import com.kakarote.core.common.Const;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ConditionalOnProperty(prefix = "wukong.auth.local", name = "enabled", havingValue = "true")
public class LocalAuthCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String cookieToken = getAuthCookieValue(request);
        boolean hasHeader = hasAuthHeader(request);
        if (!hasHeader && cookieToken != null && !cookieToken.isEmpty()) {
            filterChain.doFilter(new AuthHeaderRequestWrapper(request, cookieToken), response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getAuthCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Const.DEFAULT_TOKEN_NAME.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean hasAuthHeader(HttpServletRequest request) {
        String token = request.getHeader(Const.DEFAULT_TOKEN_NAME);
        return token != null && !token.isEmpty();
    }

}
