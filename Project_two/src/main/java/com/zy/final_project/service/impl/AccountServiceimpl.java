package com.zy.final_project.service.impl;

import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.Account;
import com.zy.final_project.dao.AccountDao;
import com.zy.final_project.service.AccountService;
import com.zy.final_project.utils.JWTUtils;
import com.zy.final_project.utils.SaltUtils;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional  //事务注解
public class AccountServiceimpl implements AccountService {

  @Autowired
  private AccountDao accountDao;
  @Autowired
  private UsersDao usersDao;
  @Override
  public Account findByUsername(String username) {
    return accountDao.findByUsername(username);
  }

  @Override
  public Long findIdByAccountName(String accountName) {
    return accountDao.findIdByAccountName(accountName);
  }

  @Override
  public Map<String, Object> register(Account account) {
    HashMap<String, Object> map = new HashMap<>();
    SnowFlakeUtils idWorker = new SnowFlakeUtils();
    try {
      //处理业务调用repository(dao)
      //1.生成随机盐
      String salt = SaltUtils.getSalt(8);
      //2.将随机盐保存到数据
      account.setSalt(salt);
      //3.明文密码进行md5 + salt + hash散列
      //密码 盐值 散列次数
      Md5Hash md5Hash = new Md5Hash(account.getPassword(),salt,1024);
      account.setPassword(md5Hash.toHex()); //toHex 转换为16进制
      account.setUserId(idWorker.getId());
//    account.setRole("ss");
      accountDao.register(account);
      System.out.println("accountName"+account.getAccountName());
      usersDao.createUser(accountDao.findIdByAccountName(account.getAccountName()),"新用户");
      HashMap<String, String> playload = new HashMap<>();
      String token = JWTUtils.getToken(playload);
      map.put("state",true);
      map.put("msg","注册成功");
      map.put("token",token);
      map.put("userId",accountDao.findUserIdByUsername(account.getAccountName()));
      return map;
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","注册失败");
      return map;
    }
  }

  @Override
  public boolean checkAccountName(String accountName) {
    System.out.println(accountName);
    try{
      if(accountDao.checkAccountName(accountName) == null){
       return true;
      }else{
        return false;
      }
    }catch (Exception e){
      return true;
    }
  }

  @Override
  public Map<String, Object> login(Account account) {
    String accountName = account.getAccountName();
    String password = account.getPassword();
    System.out.println(accountName);
    System.out.println(password);
    HashMap<String, Object> map = new HashMap<>();
    //SecurityUtils全局安全工具类
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken us = new UsernamePasswordToken(accountName,password);
    //记住我
//    token.setRememberMe(true);
    try{
      //登录成功
      subject.login(us);
      HashMap<String, String> playload = new HashMap<>();
      System.out.println();
      String token = JWTUtils.getToken(playload);
      map.put("state",true);
      map.put("msg","登陆成功");
      map.put("token",token);
      map.put("userId",accountDao.findUserIdByUsername(account.getAccountName()));
      return map;
    }catch (UnknownAccountException e){
      //用户名不存在
      map.put("state",false);
      map.put("msg","用户名不存在");
      return map;
    }catch (IncorrectCredentialsException e){
      //密码异常111
      map.put("state",false);
      map.put("msg","密码错误");
      return map;
    }
  }

  @Override
  public Map<String, Object> loginout() {
    HashMap<String, Object> map = new HashMap<>();
    Subject subject = SecurityUtils.getSubject();
    try {
      subject.logout();
      System.out.println("账户退出成功");
      map.put("state",true);
      map.put("msg","退出成功");
      return map;
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","退出失败");
    }
    return map;
  }
}
