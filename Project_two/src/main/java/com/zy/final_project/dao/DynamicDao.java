package com.zy.final_project.dao;

import com.zy.final_project.entity.dynamic.Dynamic;
import com.zy.final_project.entity.dynamic.DynamicComment;
import com.zy.final_project.entity.dynamic.DynamicReply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DynamicDao {
  int postDynamic(Dynamic dynamic);
  List<Dynamic> findDynamic(Long userId,Integer page,Integer size);
  List<Dynamic> findMyDynamic(Long userId, Integer page, Integer size);
  int postDynamicReply(DynamicReply dynamicReply);
  List<DynamicComment> findDynamicComment(Long dynamicId);
  List<DynamicComment> findDynamicCommentPage(Long dynamicId,Integer page, Integer size);
  int countReplyByDynamicId(Long dynamicId);
  int countForwardByDynamicId(Long dynamicId);
  Dynamic findDynamicByDynamicId(Long dynamicId);
  int postDynamicComment(DynamicComment dynamicComment);
  int postDynamicCommentReply(DynamicReply dynamicReply);
  Long findUserIdByCommentId(int commentId);
  List<DynamicReply> findDynamicReplyByCommentId(int commentId);
  List<DynamicReply> findDynamicAllReplyByCommentId(int commentId,Integer page,Integer size);
  int countReplyBycommentId(int commentId);
  //计算commentId在dynamic中是第几条记录
  int rankCommentInDynamic(Long dynamicId);
  int postDynamicForward(Dynamic dynamic);
  Long getForwardDynamicId(Long dynamicId);
  List<Dynamic> findDynamicForward(Long dynamicId, Integer page, Integer size);
  Integer getAllReplyNumber();
}
