package com.zy.final_project.dao;

import com.zy.final_project.entity.socket.SocketMsg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface SocketMsgDao {
  LocalDateTime checkTimeDifference(Long userId);
  LocalDateTime checkGroupTimeDifference(Long userId);
  void addPrivateMsg(SocketMsg socketMsg);
  Integer selectLastId(Long userId,Long toId);
  void updateIsLast(Integer Id);
  List<SocketMsg> findSocketMsg(Long fromUserId, Long toUserId, Integer page, Integer size);
  void addGroupMsg(SocketMsg socketMsg);
  List<SocketMsg> findSocketGroupMsg(Long toUserId, Integer page, Integer size);
}
