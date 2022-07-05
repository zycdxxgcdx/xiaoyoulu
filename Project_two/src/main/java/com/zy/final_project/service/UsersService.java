package com.zy.final_project.service;


import com.zy.final_project.entity.Users;

import java.util.Map;

public interface UsersService {
  Map<String, Object> getUserProfile(Long userId);
  Map<String, Object> updateUser(Users users);
  Map<String, Object> userInfo(Long userId);
  Map<String, Object> findAll(Long userId,Integer page,Integer size);
  Map<String, Object> follow(Long userId, Long followUserId);
  Map<String, Object> cancelFollow(Long userId,Long followUserId);
  Map<String, Object> isFollow(Long userId, Long followUserId);
  Map<String, Object> findFollowAndFansNums(Long userId);
  Map<String, Object> findFollows(Long userId, Integer page, Integer size);
  Map<String, Object> findFans(Long userId, Integer page, Integer size);
}
