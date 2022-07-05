package com.zy.final_project.service.impl;

import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.entity.Users;
import com.zy.final_project.service.UsersService;
import com.zy.final_project.utils.SensitiveWordsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UsersServiceimpl implements UsersService {

  @Autowired
  private UsersDao usersDao;

  @Override
  public Map<String, Object> getUserProfile(Long userId) {
    HashMap<String, Object> map = new HashMap<>();
    try{
      Users user = usersDao.getUserProfile(userId);
      map.put("state",true);
      map.put("msg","查找用户信息成功");
      map.put("userInfo",user);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查找用户信息异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> updateUser(Users users) {
    System.out.println(users);
    HashMap<String, Object> hashMap = new HashMap<>();
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    try{
      System.out.println(usersDao.updateUser(users));
      if(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace(users.getUsername()),1)){
        hashMap.put("state","false");
        hashMap.put("msg","昵称含有屏蔽词");
        return hashMap;
      }
      usersDao.updateUser(users);
      hashMap.put("state",true);
      hashMap.put("msg","更新用户信息成功");
    }catch (Exception e){
      hashMap.put("state",false);
      hashMap.put("msg","更新用户信息发生异常");
      e.printStackTrace();
    }
    return hashMap;
  }

  @Override
  public Map<String, Object> userInfo(Long userId) {
    HashMap<String, Object> map = new HashMap<>();
    try{
      Users user = usersDao.userInfo(userId);
      map.put("state",true);
      map.put("msg","查找用户信息成功");
      map.put("userInfo",user);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查找用户信息异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findAll(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<PublicPost> list = usersDao.findAll(userId,page,size);
      map.put("state",true);
      map.put("msg","查询用户发帖信息成功");
      map.put("userPostInfo",list);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查询用户发帖信息发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> follow(Long userId, Long followUserId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(usersDao.follow(userId,followUserId) > 0 ){
        map.put("state",true);
        map.put("msg","关注成功");
      }else{
        map.put("state",true);
        map.put("msg","关注失败");
      }
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","关注发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> cancelFollow(Long userId, Long followUserId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(usersDao.cancelFollow(userId,followUserId) > 0 ){
        map.put("state",true);
        map.put("msg","取消关注成功");
      }else{
        map.put("state",true);
        map.put("msg","取消关注失败");
      }
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","取消关注发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> isFollow(Long userId, Long followUserId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(usersDao.isFollow(userId,followUserId)!=null ){
        map.put("state",true);
        map.put("msg","已关注");
      }else{
        map.put("state",true);
        map.put("msg","未关注");
      }
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查询关注发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findFollowAndFansNums(Long userId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      Integer followNums = usersDao.findFollowNums(userId);
      Integer fansNums = usersDao.findFansNums(userId);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("followNums",followNums);
      map.put("fansNums",fansNums);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findFollows(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Users> followUsers = usersDao.findFollows(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("followUsers",followUsers);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findFans(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Users> fanUsers = usersDao.findFans(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("fanUsers",fanUsers);
    }catch (Exception e){
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }
}
