package com.ecommerce.config;

import com.ecommerce.shiro.JwtFilter;
import com.ecommerce.shiro.JwtRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置：JWT 无状态认证 + 路径权限。
 * 禁用 Session 存储，每次请求都通过 JwtFilter 重新认证，实现真正的无状态。
 */
@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(jwtRealm);

        // 禁用 Session 存储，实现无状态 JWT 认证
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
        bean.setFilters(filters);

        // 路径权限：公开接口用 anon，其余 /api/** 需 JWT 认证
        Map<String, String> chain = new LinkedHashMap<>();
        chain.put("/api/auth/login", "anon");
        chain.put("/api/auth/register", "anon");
        chain.put("/api/products", "anon");
        chain.put("/api/products/**", "anon");
        chain.put("/api/reviews/product/**", "anon");
        chain.put("/uploads/**", "anon");
        chain.put("/api/**", "jwt");
        bean.setFilterChainDefinitionMap(chain);

        return bean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
