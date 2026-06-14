package com.ecommerce.util;

import com.ecommerce.exception.BusinessException;
import com.ecommerce.shiro.UserPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 从 Shiro Subject 获取当前登录用户信息。
 * 完全无状态：所有信息来自 JWT 解析后的 UserPrincipal，不依赖 Session。
 */
public final class UserContext {

    private UserContext() {
    }

    private static Subject subject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录用户 ID，未登录时抛出 BusinessException(401)。
     */
    public static Long currentUserId() {
        UserPrincipal principal = getPrincipal();
        if (principal == null) {
            throw new BusinessException(401, "未登录");
        }
        return principal.getUserId();
    }

    /**
     * 获取当前用户角色，未登录时返回 "guest"。
     */
    public static String currentRole() {
        UserPrincipal principal = getPrincipal();
        return principal != null ? principal.getRole() : "guest";
    }

    /**
     * 判断当前用户是否为管理员。
     */
    public static boolean isAdmin() {
        String role = currentRole();
        return "admin".equalsIgnoreCase(role);
    }

    private static UserPrincipal getPrincipal() {
        if (!subject().isAuthenticated()) {
            return null;
        }
        Object principal = subject().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }
        return null;
    }
}
