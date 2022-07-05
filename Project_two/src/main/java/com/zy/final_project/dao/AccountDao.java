package com.zy.final_project.dao;

import com.zy.final_project.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
//扫入service容器里
@Repository
//添加到ioc容器中
public interface AccountDao {
  //根据用户名查找账号
  Account findByUsername(String accountName);
  //查找用户Id
  Long findIdByAccountName(String accountName);
  //注册功能
  void register(Account account);
  //用户名是否重复校验
  Long checkAccountName(String accountName);

  String findUsernameById(Long userId);
  //根据用户名查找账号   将userid放入Account类中 使Long型数据不丢失精度
  Account findUserIdByUsername(String accountName);
  Integer getAllUserNumber();
}
