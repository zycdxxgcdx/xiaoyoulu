package com.zy.final_project.dao;

import com.zy.final_project.entity.dynamic.DynamicLikeNums;
import com.zy.final_project.entity.dynamic.DynamicLikes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DynamicLikesDao {
  Integer checkLikeIsExist(DynamicLikes dynamicLikes);
  Integer saveDynamicLikes(DynamicLikes dynamicLikes);
  Integer updateDynamicLikes(DynamicLikes dynamicLikes);

  Integer checkLikeNumsIsExist(DynamicLikeNums dynamicLikeNums);
  Integer saveDynamicLikeNums(DynamicLikeNums dynamicLikeNums);
  Integer updateDynamicLikeNums(DynamicLikeNums dynamicLikeNums);
}
