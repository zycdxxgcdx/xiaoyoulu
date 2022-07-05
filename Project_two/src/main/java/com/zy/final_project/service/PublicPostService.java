package com.zy.final_project.service;

import com.zy.final_project.entity.publicpost.Emoji;
import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.entity.publicpost.Reply;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface PublicPostService {
  List<PublicPost> findAll(Integer page, Integer size);
  int findAllPages();
  Map<String,Object> findPost(long publicpostid);
  Map<String,Object> findSearchPost(String input, Integer page, Integer size);
  Integer findPostView(Long publicpostid);
  void addPostView(Long publicpostid);
  void addPostViewInMysql();
  boolean publicpostComment(Map<String, Object> map);
  List<Emoji> findpublicostComment(Long publicpostid,Integer page,Integer size);
  int countPublicostComment(Long publicpostid);
  Map<String, Object> reply(Map<String, Object> map);
  List<Reply> findreply(Long publicpostid, Integer page, Integer size);
  List<Reply> findReplyByCommentId(Long publicpostid,Integer commentId,Integer commentPage,
                                   Integer replyPage,Integer commentpagesize,Integer replypagesize);
  Map<String, Object> getStatisticalInfo();
}
