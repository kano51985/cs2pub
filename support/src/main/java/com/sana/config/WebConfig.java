package com.sana.config;


import com.sana.interceptor.PreRequestInterceptor;
import jakarta.annotation.Resource;
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
public class WebConfig extends WebMvcConfigurationSupport {

    @Resource
    private PreRequestInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**").
                excludePathPatterns("/auth/**");
    }

}
