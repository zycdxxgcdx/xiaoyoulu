package com.zy.final_project.service;

import com.zy.final_project.entity.socket.SocketMsg;

import java.util.Map;

public interface SocketService {
  boolean checkTimeDifference(SocketMsg socketMsg,Integer index);
  boolean addPrivateMsg(SocketMsg socketMsg);
  boolean addGroupMsg(SocketMsg socketMsg);
  Map<String,Object> findSocketList(Long userId, Integer page, Integer size);
  Map<String,Object> delListUser(Long fromUserId, Long toUserId);
  Map<String,Object> findSocketMsg(Long fromUserId, Long toUserId,Integer page, Integer size);
  Map<String,Object> addSocketList(Long fromUserId, Long toUserId, Integer type);
  Map<String,Object> findSocketGroupMsg(Long toUserId, Integer page, Integer size);
}
