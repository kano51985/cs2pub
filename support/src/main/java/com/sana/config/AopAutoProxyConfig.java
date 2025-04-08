package com.sana.config;


import cn.hutool.aop.ProxyUtil;
import com.sana.aspect.PermissionCheckAspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-08
 * @Description: Aop代理配置类（自动扫描包版）
 * @Version: 1.0
 */

@Configuration
public class AopAutoProxyConfig {
    @Autowired
    private PermissionCheckAspect permissionCheckAspect;

    /**
     * 自动代理所有Controller的Bean后处理器
     */
    @Bean
    public BeanPostProcessor controllerProxyPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                // 检查是否是Controller(包含@Controller和@RestController)
                Class<?> beanClass = bean.getClass();
                if (beanClass.isAnnotationPresent(RestController.class)) {
                    // 创建代理
                    return ProxyUtil.proxy(bean, permissionCheckAspect);
                }
                return bean;
            }
        };
    }
}
