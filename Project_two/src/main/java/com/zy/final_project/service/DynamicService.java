package com.zy.final_project.service;


import com.zy.final_project.entity.dynamic.Dynamic;
import com.zy.final_project.entity.dynamic.DynamicComment;
import com.zy.final_project.entity.dynamic.DynamicReply;


import java.util.Map;

public interface DynamicService {
  Map<String, Object> postDynamic(Map<String,Object> map);
  Map<String, Object> findDynamic(Long userId,Integer page,Integer size);
  Map<String, Object> findMyDynamic(Long userId, Integer page, Integer size);
  Map<String,Object> postDynamicReply(DynamicReply dynamicReply);
  Map<String,Object> findDynamicComment(Long dynamicId);
  Map<String,Object> findDynamicComment(Long dynamicId, Integer page, Integer size);
  Map<String,Object> findDynamicByDynamicId(Long dynamicId);
  Map<String,Object> findDynamicReply(Long dynamicId);
  Map<String,Object> findDynamicMoreReply(Long dynamicId, Integer page, Integer size);
  Map<String,Object> postDynamicComment(DynamicComment dynamicComment);
  Map<String,Object> postDynamicCommentReply(DynamicReply dynamicReply);
  Map<String,Object> findDynamicReply(Long dynamicId,Integer commentId,Integer page,Integer size);
  Map<String,Object> postDynamicForward(Dynamic dynamic);
  Map<String,Object> findDynamicForward(Long dynamicId, Integer page, Integer size);
}
