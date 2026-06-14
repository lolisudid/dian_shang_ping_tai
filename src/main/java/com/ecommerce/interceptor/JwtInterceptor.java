package com.ecommerce.interceptor;

import com.ecommerce.common.Result;
import com.ecommerce.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器：从 Authorization 头提取 Bearer Token 并验证。
 * 替代 Shiro，更轻量且完全无状态。
 */
public class JwtInterceptor implements HandlerInterceptor {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JwtUtils jwtUtils;

    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Claims claims = jwtUtils.parse(token);
            request.setAttribute("userId", Long.valueOf(claims.get("userId").toString()));
            request.setAttribute("username", claims.get("username").toString());
            request.setAttribute("role", claims.get("role").toString());
            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            MAPPER.writeValue(response.getWriter(), Result.fail(401, "未登录或 Token 已失效"));
            return false;
        }
    }
}
