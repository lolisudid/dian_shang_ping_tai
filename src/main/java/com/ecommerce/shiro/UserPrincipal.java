package com.ecommerce.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * JWT 封装分用户主体信息，作为 Shiro Subject 的 Principal，
 * 包含 userId、username、role，无需依赖 Session 存储。
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String role;

    @Override
    public String toString() {
        return String.valueOf(userId);
    }
}
