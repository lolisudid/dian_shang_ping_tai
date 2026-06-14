package com.ecommerce.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Shiro 自定义 token，承载 JWT 字符串。
 */
public class JwtToken implements AuthenticationToken {

    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }
}
