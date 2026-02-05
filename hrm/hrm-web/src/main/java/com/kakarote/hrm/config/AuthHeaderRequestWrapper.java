package com.kakarote.hrm.config;

import com.kakarote.core.common.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class AuthHeaderRequestWrapper extends HttpServletRequestWrapper {
    private final String token;

    public AuthHeaderRequestWrapper(HttpServletRequest request, String token) {
        super(request);
        this.token = token;
    }

    @Override
    public String getHeader(String name) {
        if (Const.DEFAULT_TOKEN_NAME.equalsIgnoreCase(name)) {
            return token;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (Const.DEFAULT_TOKEN_NAME.equalsIgnoreCase(name)) {
            return Collections.enumeration(Collections.singletonList(token));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>();
        Enumeration<String> original = super.getHeaderNames();
        while (original.hasMoreElements()) {
            names.add(original.nextElement());
        }
        names.add(Const.DEFAULT_TOKEN_NAME);
        return Collections.enumeration(names);
    }
}
