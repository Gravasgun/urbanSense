package com.cqupt.urbansense.filter;

import com.cqupt.urbansense.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizeFilter implements Filter, Ordered {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();
        log.info("filter path:{}", path);
        // 如果是登录请求，直接放行
        if (path.contains("/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 获取 token
        String token = request.getHeader("token");
        // 如果 token 为空，设置 401 状态码并结束响应
        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 校验 token
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claimsBody);
            String storedToken = redisTemplate.opsForValue().get("token");
            //token无效或者redis中的accessKey不存在或过期
            if ((result == 1 || result == 2) && StringUtils.isBlank(storedToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // token 有效，放行请求
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码
    }

    @Override
    public void destroy() {
        // 释放资源
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
