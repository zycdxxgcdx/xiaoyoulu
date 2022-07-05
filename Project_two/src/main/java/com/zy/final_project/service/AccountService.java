package com.zy.final_project.service;

import com.zy.final_project.entity.Account;

import java.util.Map;

public interface AccountService {
  Account findByUsername(String username);
  Long findIdByAccountName(String accountName);
  Map<String, Object> register(Account account); //注册
  boolean checkAccountName(String accountName);//检查用户注册时用户名是否重复(数据库中已存在该用户名)
  Map<String,Object> login(Account account);  //用户登录
  Map<String,Object> loginout();  //用户退出登录
}
