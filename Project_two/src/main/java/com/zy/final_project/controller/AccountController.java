package com.zy.final_project.controller;

import com.zy.final_project.entity.Account;
import com.zy.final_project.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {
  @Autowired
  private AccountService accountService;
  @GetMapping("/checkAccountState")
  public Map<String, Object> register(){
    HashMap<String, Object> map = new HashMap<>();
    map.put("state",true);
    map.put("msg","账号登录状态检测成功");
    return map;
  }
  @GetMapping("/{url}")
  public String redirect(@PathVariable("url") String url){
    return url;
  }
  // 检查用户名是否重复
  @GetMapping("/checkAccountName/{accountName}")
  public boolean check(@PathVariable("accountName") String accountName){
   return accountService.checkAccountName(accountName);
  }
  //注册
  @PostMapping("/register")
  public Map<String, Object> register(@RequestBody Account account){
    return accountService.register(account);
  }
  //登录
  @PostMapping("/login")
  public Map<String,Object> login(@RequestBody Account account){
    return accountService.login(account);
  }

  @GetMapping("/unauth")
  @ResponseBody
  public String unauth(){
    return "未授权，无法访问";
  }

  //账号退出
  @GetMapping("/logout")
  public Map<String, Object> logout(){
   return accountService.loginout();
  }
}
