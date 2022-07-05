package com.zy.final_project.dao;

import com.zy.final_project.entity.dynamic.DynamicLikes;
import com.zy.final_project.entity.message.MessageClass;
import com.zy.final_project.entity.message.MessageExitClass;
import com.zy.final_project.entity.message.MessageLike;
import com.zy.final_project.entity.message.MessageReply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
@Repository
public interface MessageDao {
  List<MessageReply> findPostInfo(Long userId, Integer page, Integer size);
  List<MessageReply> findDynamicInfo(Long userId, Integer page, Integer size);
  List<MessageReply> findReplyInfo(Long userId, Integer page, Integer size);
  List<MessageClass> findClassApply(Long userId, Integer page, Integer size);
  Integer classApplyAgree(Integer Id, LocalDateTime passTime);
  MessageClass findMessageClassById(Integer Id);
  Integer classApplyReject(Integer Id,LocalDateTime passTime);
  List<MessageExitClass> findClassNotify(Long userId,Integer page,Integer size);
  List<MessageLike> findLikeInfo(List<DynamicLikes> dynamicLikes, Long userId,Integer page, Integer size);
}
