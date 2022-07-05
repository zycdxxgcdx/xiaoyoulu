package com.zy.final_project.service;

import com.zy.final_project.entity.dynamic.DynamicLikes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DynamicLikesService {

  Map<String, Object> saveLiked2Redis(String userId, String dynamicId);
  boolean checklike(String userId, String dynamicId);
  /**
   * 取消点赞。将状态改变为0
   * @param likedUserId
   * @param likedPostId
   */
  Map<String, Object> unlikeFromRedis(String likedUserId, String likedPostId);

  /**
   * 从Redis中删除一条点赞数据
   */
  HashMap<String, Object> deleteLikedFromRedis(String userId,String dynamicId);

  /**
   * 该用户的点赞数加1
   * @param likedUserId
   */
  void incrementLikedCount(String likedUserId);

  /**
   * 该用户的点赞数减1
   * @param likedUserId
   */
  void decrementLikedCount(String likedUserId);

  /**
   * 获取Redis中存储的所有点赞数据
   * @return
   */
  List<DynamicLikes> getLikedDataFromRedis(Long userId);

  /**
   * 获取Redis中存储的所有点赞数量
   * @return
   */
  Integer getLikedCountFromRedis(String dynamicId);

  //保存点赞记录
  void saveDynamicLikes();

  //保存每条动态点赞数
  void saveDynamicLikesNums();
}
