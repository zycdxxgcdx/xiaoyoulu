package com.zy.final_project.config;

import com.zy.final_project.filter.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  //拦截请求中的token
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new JwtTokenInterceptor()).
            excludePathPatterns("/ssss/**")//那些路径不用拦截
            .addPathPatterns("/post/**")//那些路径需要拦截
            .addPathPatterns("/account/checkAccountState")
            .addPathPatterns("/publicpost/publicpostComment");
  }
}
