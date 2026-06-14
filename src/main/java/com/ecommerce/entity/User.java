package com.ecommerce.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体，对应表 sys_user。
 * role：user 普通用户，admin 管理员。
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    /** 角色：user / admin */
    private String role;
    private String createTime;
}
