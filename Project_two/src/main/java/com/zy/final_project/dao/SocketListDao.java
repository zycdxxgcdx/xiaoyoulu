package com.zy.final_project.dao;

import com.zy.final_project.entity.socket.SocketList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
@Repository
public interface SocketListDao {
  List<SocketList> findSocketList(Long userId, Integer page, Integer size);
  Integer delListUser(Long fromUserId, Long toUserId, LocalDateTime time);
  Integer checkSocketListExist(Long fromUserId, Long toUserId);
  Integer updateSocketListState(Long fromUserId, Long toUserId,LocalDateTime time,Integer type);
  Integer addSocketList(Long fromUserId, Long toUserId,LocalDateTime time,Integer type);
}
