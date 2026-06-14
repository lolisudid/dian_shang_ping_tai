package com.ecommerce.util;

import com.ecommerce.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 从当前请求中获取登录用户信息（由 JwtInterceptor 注入）。
 */
public final class UserContext {

    private UserContext() {}

    private static HttpServletRequest currentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new BusinessException(500, "无法获取请求上下文");
        }
        return attrs.getRequest();
    }

    public static Long currentUserId() {
        Object id = currentRequest().getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    public static String currentRole() {
        Object role = currentRequest().getAttribute("role");
        return role != null ? role.toString() : "guest";
    }

    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(currentRole());
    }
}
