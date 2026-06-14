package com.ecommerce.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求日志拦截器：记录 URL、方法、IP、参数与耗时（符合 requirements 示例格式）。
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        String query = request.getQueryString();
        String params = query != null ? query : "";
        String ip = request.getRemoteAddr();
        System.out.printf("[Request] %s %s | IP: %s | Params: %s%n",
                request.getMethod(), request.getRequestURI(), ip, params.isEmpty() ? "-" : params);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long start = (Long) request.getAttribute(START_TIME);
        long cost = start != null ? System.currentTimeMillis() - start : 0;
        System.out.printf("[Response] %d | Time: %dms%n", response.getStatus(), cost);
    }
}
