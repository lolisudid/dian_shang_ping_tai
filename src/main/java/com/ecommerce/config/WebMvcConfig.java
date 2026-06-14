package com.ecommerce.config;

import com.ecommerce.interceptor.CorsInterceptor;
import com.ecommerce.interceptor.JwtInterceptor;
import com.ecommerce.interceptor.RequestLoggingInterceptor;
import com.ecommerce.util.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：注册拦截器链。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;

    public WebMvcConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // CORS 跨域
        registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/api/**");
        // 请求日志
        registry.addInterceptor(new RequestLoggingInterceptor()).addPathPatterns("/api/**");
        // JWT 认证（排除公开接口）
        registry.addInterceptor(new JwtInterceptor(jwtUtils))
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/products",
                        "/api/products/**",
                        "/api/reviews/product/**",
                        "/uploads/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}
