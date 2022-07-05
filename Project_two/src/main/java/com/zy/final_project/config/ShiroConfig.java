package com.zy.final_project.config;

import com.zy.final_project.realm.AccountRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro配置
 * 将realm加载进shiro中
 */
@Configuration
public class ShiroConfig {

  //负责拦截所有请求
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager securityManager){
    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
    factoryBean.setSecurityManager(securityManager);
    //权限设置
    Map<String,String> map = new HashMap<>();
    //anon 公共资源
//    map.put("/main","authc");   authc请求这个资源需要认证和授权
//     map.put("/manage","perms[manage]");
//    map.put("/administrator","roles[administrator]");
//    map.put("/logout","logout");
    factoryBean.setFilterChainDefinitionMap(map);

    //设置登录页面
    //未登录自动跳转到该页面，如果被拦截了的话
    //默认认证界面路径
//    factoryBean.setLoginUrl("/login");
    //设置未授权页面
//    factoryBean.setUnauthorizedUrl("/unauth");
    return factoryBean;
  }

  //注入到上面的方法中
  //@Qualifier 指定注入的容器名 就是下面方法中的bean，一般是方法名
  @Bean
  public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("accountRealm") AccountRealm accountRealm){
    //创建安全管理器对象
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    //给安全管理器设置realm
    manager.setRealm(accountRealm);
    //设置session
    manager.setSessionManager(getDefaultWebSessionManager());
    return manager;
  }

  //注入到上面的方法中
  @Bean
  public AccountRealm accountRealm(){
      //md5加密验证 md5 + salt + 散列次数
    AccountRealm realm = new AccountRealm();
    //修改凭证校验匹配器
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    //设置加密算法为MD5
    credentialsMatcher.setHashAlgorithmName("md5");
    //散列次数
    credentialsMatcher.setHashIterations(1024);
    realm.setCredentialsMatcher(credentialsMatcher);
    return realm;
  }

  @Bean
  public DefaultWebSessionManager getDefaultWebSessionManager(){
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    // 设置全局session过期时间
    sessionManager.setGlobalSessionTimeout(15*1000);
    return sessionManager;
  }
}

