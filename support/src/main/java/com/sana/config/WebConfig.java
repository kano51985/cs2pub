package com.sana.config;


import com.sana.interceptor.PreRequestInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: MVC拦截器
 * @Version: 1.0
 */

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<PreRequestInterceptor> preRequestFilter(PreRequestInterceptor interceptor) {
        FilterRegistrationBean<PreRequestInterceptor> bean = new FilterRegistrationBean<>();
        bean.setFilter(interceptor);
        // 拦截所有路径
        bean.addUrlPatterns("/auth/test");
        // 设置最高优先级
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
