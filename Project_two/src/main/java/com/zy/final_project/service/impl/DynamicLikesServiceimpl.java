package com.zy.final_project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.zy.final_project.dao.DynamicLikesDao;
import com.zy.final_project.entity.dynamic.DynamicLikeNums;
import com.zy.final_project.entity.dynamic.DynamicLikes;
import com.zy.final_project.service.DynamicLikesService;
import com.zy.final_project.utils.DynamicLikesUtils;
import com.zy.final_project.utils.SerializeUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional  //事务注解
public class DynamicLikesServiceimpl implements DynamicLikesService {
  @Autowired
  RedisTemplate redisTemplate;
  @Autowired
  private DynamicLikesDao dynamicLikesDao;
//  @Autowired
//  LikedService likedService;
  @Override
  public Map<String, Object> saveLiked2Redis(String userId, String dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      String key = DynamicLikesUtils.getLikedKey(userId,dynamicId);
      redisTemplate.opsForHash().put(DynamicLikesUtils.MAP_KEY_USER_LIKED, key, 1);
//      redisTemplate.opsForHash().put(DynamicLikesUtils.MAP_KEY_USER_LIKED, key, dynamicLikes);
      this.incrementLikedCount(dynamicId);
      map.put("state",true);
      map.put("msg","点赞成功");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","点赞异常");
    }
    return map;
  }

  @Override
  public boolean checklike(String userId, String dynamicId) {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
    while (cursor.hasNext()){
      Map.Entry<Object, Object> entry = cursor.next();
//      System.out.println("+++++++++++++++++++++++++++++++");
//      System.out.println(entry);
//      System.out.println(entry.getKey());
//      System.out.println(entry.getValue());
//      System.out.println("+++++++++++++++++++++++++++++++");
      String key = (String) entry.getKey();
      Integer value = (Integer) entry.getValue();
      //分离出 likedUserId，likedPostId
      String[] split = key.split("::");
      String userIds = split[0];
      String dynamicIds = split[1];
      if(userId.equals(userIds) && dynamicId.equals(dynamicIds) && value==1){
        return true;
      }
    }
    return false;
  }

  @Override
  public Map<String, Object> unlikeFromRedis(String userId,String dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      String key = DynamicLikesUtils.getLikedKey(userId, dynamicId);
      redisTemplate.opsForHash().put(DynamicLikesUtils.MAP_KEY_USER_LIKED, key, 0);
      this.decrementLikedCount(dynamicId);
      map.put("state",true);
      map.put("msg","取消点赞成功");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","取消点赞异常");
    }
    return map;
  }

  @Override
  public HashMap<String, Object> deleteLikedFromRedis(String userId,String dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      String key = DynamicLikesUtils.getLikedKey(userId, dynamicId);
      redisTemplate.opsForHash().delete(DynamicLikesUtils.MAP_KEY_USER_LIKED, key);
      map.put("state",true);
      map.put("msg","删除成功");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","取消点赞异常");
    }
    return map;
  }

  @Override
  public void incrementLikedCount(String dynamicId) {
    redisTemplate.opsForHash().increment(DynamicLikesUtils.MAP_KEY_USER_LIKED_COUNT, dynamicId, 1);
  }

  @Override
  public void decrementLikedCount(String dynamicId) {
    redisTemplate.opsForHash().increment(DynamicLikesUtils.MAP_KEY_USER_LIKED_COUNT, dynamicId, -1);
  }

  @Override
  public List<DynamicLikes> getLikedDataFromRedis(Long userId) {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
    List<DynamicLikes> list = new ArrayList<>();
    while (cursor.hasNext()){
      Map.Entry<Object, Object> entry = cursor.next();
      String key = (String) entry.getKey();
      //分离出 likedUserId，likedPostId
      String[] split = key.split("::");
      String userIds = split[0];
      String dynamicId = split[1];
      Integer value = (Integer) entry.getValue();
      if(value == 1 && Long.parseLong(userIds)!= userId){
        DynamicLikes dynamicLikes = new DynamicLikes();
        dynamicLikes.setUserId(userIds);
        dynamicLikes.setDynamicId(dynamicId);
        dynamicLikes.setStatus(value);
        list.add(dynamicLikes);
      }
    }
    return list;
  }

  @Override
  public Integer getLikedCountFromRedis(String dynamicId) {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
//    List<LikedCountDTO> list = new ArrayList<>();
    while (cursor.hasNext()){
      Map.Entry<Object, Object> map = cursor.next();
//      System.out.println(map);
      //将点赞数量存储在 LikedCountDT
      String key = (String)map.getKey();
      if(key.equals(dynamicId)){
        return (Integer) map.getValue();
      }
//      if(key.equals(dynamicId)){
//        return (Integer) map.getValue();
//      }
//      LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());
//      list.add(dto);
      //从Redis中删除这条记录
//      redisTemplate.opsForHash().delete(DynamicLikesUtils.MAP_KEY_USER_LIKED_COUNT, key);
    }
//    return list;
    return 0;
  }


  //从redis取出点赞信息数据存储mysql中
  @Override
  public void saveDynamicLikes() {
    try {
      Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
      while (cursor.hasNext()){
        Map.Entry<Object, Object> entry = cursor.next();
        String key = (String) entry.getKey();
        //分离出 likedUserId，likedPostId
        String[] split = key.split("::");
        String userId = split[0];
        String dynamicId = split[1];
        Integer value = (Integer) entry.getValue();
        DynamicLikes dynamicLikes = new DynamicLikes();
        dynamicLikes.setUserId(userId);
        dynamicLikes.setDynamicId(dynamicId);
        dynamicLikes.setStatus(value);
        if(dynamicLikesDao.checkLikeIsExist(dynamicLikes) == null){
          dynamicLikesDao.saveDynamicLikes(dynamicLikes);
        }else{
          dynamicLikesDao.updateDynamicLikes(dynamicLikes);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override
  public void saveDynamicLikesNums() {
    try {
      Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
      while (cursor.hasNext()){
        Map.Entry<Object, Object> map = cursor.next();
        DynamicLikeNums dynamicLikeNums = new DynamicLikeNums();
        dynamicLikeNums.setDynamicId((String)map.getKey());
        dynamicLikeNums.setLikenum((Integer) map.getValue());
        if(dynamicLikesDao.checkLikeNumsIsExist(dynamicLikeNums) == null){
          dynamicLikesDao.saveDynamicLikeNums(dynamicLikeNums);
        }else {
          dynamicLikesDao.updateDynamicLikeNums(dynamicLikeNums);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
