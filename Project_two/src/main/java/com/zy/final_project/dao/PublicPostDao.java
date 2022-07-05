package com.zy.final_project.dao;

import com.zy.final_project.entity.publicpost.Emoji;
import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.entity.publicpost.PublicPostComment;
import com.zy.final_project.entity.publicpost.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface PublicPostDao {
  List<PublicPost> findAll(Integer page, Integer size);
  int findAllPages();
  PublicPost findPost(long publicpostid);
  List<PublicPost> findSearchPost(String input, Integer page, Integer size);
  Integer countSearchPost(String input);
  void addPostViewInMysql(Long publicpostid, Integer view);
  int publicpostComment(PublicPostComment publicPostComment);
  List<Emoji> findpublicostComment(long publicpostid,Integer page,Integer size);
  int countPublicostComment(Long publicpostid);
  int reply(Reply reply);
  List<Reply> findreply(Integer commentId);
  int countReply(int commentId);
  List<Reply> findReplyByCommentId(Integer commentId,Integer page,Integer size);
  Integer getAllPostNumber();
  Integer getAllReplyNumber();
}
