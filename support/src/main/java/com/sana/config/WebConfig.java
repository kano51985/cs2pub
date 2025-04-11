package com.sana.config;


import com.sana.interceptor.PreRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: MVC拦截器
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class WebConfig{
    @Bean
    public FilterRegistrationBean<PreRequestInterceptor> preRequestFilter(PreRequestInterceptor interceptor) {
        FilterRegistrationBean<PreRequestInterceptor> bean = new FilterRegistrationBean<>();
        bean.setFilter(interceptor);
        // 拦截指定路径，应该拦截所有请求，放开auth请求
        bean.addUrlPatterns("/forum/**","/topic/**","/repl**");
        // 设置最高优先级
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
