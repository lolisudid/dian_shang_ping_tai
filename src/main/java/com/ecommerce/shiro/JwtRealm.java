package com.ecommerce.shiro;

import com.ecommerce.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Shiro Realm：校验 JWT 并注入角色权限。
 * 完全无状态：不依赖 Session，所有信息来自 JWT 本身。
 */
@Component
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证：解析 JWT，构造 UserPrincipal 作为 Principal，
     * 使 UserContext 可以直接从 Subject 获取用户信息。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String token = ((JwtToken) authenticationToken).getToken();
        try {
            Claims claims = jwtUtils.parse(token);
            Long userId = Long.valueOf(claims.get("userId").toString());
            String username = claims.get("username").toString();
            String role = claims.get("role").toString();

            // 将 userId/username/role 封装为 UserPrincipal 存入 Principal
            UserPrincipal principal = new UserPrincipal(userId, username, role);
            return new SimpleAuthenticationInfo(principal, token, getName());
        } catch (Exception e) {
            throw new AuthenticationException("Token 无效或已过期");
        }
    }

    /**
     * 授权：从 Principal 中提取 role，赋予 admin 角色标识。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserPrincipal principal = (UserPrincipal) principals.getPrimaryPrincipal();
        if (principal != null && principal.getRole() != null) {
            info.addRole(principal.getRole());
        }
        return info;
    }
}
