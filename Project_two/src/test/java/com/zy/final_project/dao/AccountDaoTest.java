package com.zy.final_project.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountDaoTest {
  @Autowired
  private AccountDao accountDao;
  @Test
  void findByUsername() {
    System.out.println(accountDao.findByUsername("zs"));
  }
}