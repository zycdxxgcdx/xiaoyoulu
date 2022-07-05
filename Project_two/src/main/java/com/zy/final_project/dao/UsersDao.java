package com.zy.final_project.dao;

import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.entity.Users;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface UsersDao {
  Users getUserProfile(Long userId);
  //新建一个用户信息   用户注册时自动向user表插入一条用户信息
  void createUser(Long userId,String username);
  //根据userId查找用户名
  String findUsernameById(Long userId);
  //用户修改个人信息
  int updateUser(Users user);
  //根据userId查找用户信息
  Users userInfo(Long userId);
  //更具userId查询其发过的帖子
  List<PublicPost> findAll(Long userId, Integer page, Integer size);
  List<Users> findUsersByUserId(Long userId);
  Integer follow(Long userId, Long followUserId);
  Integer cancelFollow(Long userId, Long followUserId);
  Integer isFollow(Long userId, Long followUserId);
  Integer findFollowNums(Long userId);
  Integer findFansNums(Long userId);
  List<Users> findFollows(Long userId, Integer page, Integer size);
  List<Users> findFans(Long userId, Integer page, Integer size);
}
