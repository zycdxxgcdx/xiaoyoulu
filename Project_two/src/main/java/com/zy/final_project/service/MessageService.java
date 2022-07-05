package com.zy.final_project.service;

import java.util.Map;

public interface MessageService {
  Map<String, Object> findPostInfo(Long userId, Integer page, Integer size);
  Map<String, Object> findDynamicInfo(Long userId, Integer page, Integer size);
  Map<String, Object> findReplyInfo(Long userId, Integer page, Integer size);
  Map<String, Object> findClassApply(Long userId, Integer page, Integer size);
  Map<String, Object> classApplyAgree(Integer Id);
  Map<String, Object> findClassApplyOne(Integer Id);
  Map<String, Object> classApplyReject(Integer Id);
  Map<String, Object> findClassNotify(Long userId,Integer page,Integer size);
  Map<String, Object> findLikeInfo(Long userId, Integer page, Integer size);
}
