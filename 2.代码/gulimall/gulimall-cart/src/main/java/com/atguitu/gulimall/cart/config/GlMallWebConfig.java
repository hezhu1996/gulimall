package com.atguitu.gulimall.cart.config;

import com.atguitu.gulimall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlMallWebConfig implements WebMvcConfigurer {
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册列表中添加拦截器
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
