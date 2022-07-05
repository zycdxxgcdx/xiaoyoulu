package com.zy.final_project.service.impl;

import com.zy.final_project.dao.*;
import com.zy.final_project.entity.publicpost.Emoji;
import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.entity.publicpost.PublicPostComment;
import com.zy.final_project.entity.publicpost.Reply;
import com.zy.final_project.service.AccountService;
import com.zy.final_project.service.PublicPostService;
import com.zy.final_project.utils.DynamicLikesUtils;
import com.zy.final_project.utils.SensitiveWordsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PublicPostServiceimpl implements PublicPostService {
  @Autowired
  RedisTemplate redisTemplate;
  @Autowired
  private PublicPostDao publicPostDao;
  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountDao accountDao;
  @Autowired
  private UsersDao usersDao;
  @Autowired
  private DynamicDao dynamicDao;
  @Autowired
  private ClassesDao classesDao;
  @Autowired
  private SchoolDao schoolDao;
  @Override
  public List<PublicPost> findAll(Integer page, Integer size) {
    List<PublicPost> publicPosts = publicPostDao.findAll(page,size);
    System.out.println("page:"+page+"   size:"+size);
    System.out.println(publicPosts);
    for(PublicPost item:publicPosts){
      item.setViews(this.findPostView(item.getPublicpostid()));
    }
    return publicPosts;
  }

  @Override
  public int findAllPages() {
    return publicPostDao.findAllPages();
  }


  @Override
  public Map<String,Object> findPost(long publicpostid) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      System.out.println(publicPostDao.findPost(publicpostid));
      this.addPostView(publicpostid);
      PublicPost publicPost = publicPostDao.findPost(publicpostid);
      publicPost.setViews(this.findPostView(publicpostid));
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("publicpost",publicPost);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询异常");
    }
    return map;
  }


  @Override
  public Map<String,Object> findSearchPost(String input, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<PublicPost> publicPost = publicPostDao.findSearchPost(input,page,size);
      System.out.println(publicPost);
      Integer publicPostNums = publicPostDao.countSearchPost(input);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("publicPost",publicPost);
      map.put("publicPostNums",publicPostNums);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询异常");
    }
    return map;
  }

  @Override
  public Integer findPostView(Long publicpostid) {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.POST_VIEW_COUNT, ScanOptions.NONE);
    while (cursor.hasNext()){
      Map.Entry<Object, Object> entry = cursor.next();
      if(Long.parseLong(entry.getKey().toString()) == publicpostid){
        return (Integer) entry.getValue();
      }
    }
    return 0;
  }

  @Override
  public void addPostView(Long publicpostid) {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.POST_VIEW_COUNT, ScanOptions.NONE);
    boolean isAddPostView = true;
    if(!cursor.hasNext()){
      //cursor为空的时候 直接插入view
      redisTemplate.opsForHash().put(DynamicLikesUtils.POST_VIEW_COUNT, publicpostid, 1);
    }else {
      while (cursor.hasNext()){
        Map.Entry<Object, Object> entry = cursor.next();
        if(Long.parseLong(entry.getKey().toString()) == publicpostid){
          //如果redis中有该帖子id 则修改
          redisTemplate.opsForHash().increment(DynamicLikesUtils.POST_VIEW_COUNT, publicpostid, 1);
          isAddPostView = false;
          break;
        }
      }
      if(isAddPostView){
        //redis中无该帖子id
        redisTemplate.opsForHash().put(DynamicLikesUtils.POST_VIEW_COUNT, publicpostid, 1);
      }
    }
  }

  @Override
  public void addPostViewInMysql() {
    Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(DynamicLikesUtils.POST_VIEW_COUNT, ScanOptions.NONE);
    while (cursor.hasNext()){
      Map.Entry<Object, Object> entry = cursor.next();
      publicPostDao.addPostViewInMysql(Long.parseLong(entry.getKey().toString()),(Integer)entry.getValue());
    }
  }

  @Override
  public boolean publicpostComment(Map<String, Object> map) {
    HashMap<String, Object> map1 = new HashMap<>();
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    map1 = (HashMap)map.get("comment");
    PublicPostComment postComment = new PublicPostComment();
    postComment.setUserId(accountService.findIdByAccountName(String.valueOf(map1.get("accountName"))));
    postComment.setPublicpostid(Long.parseLong(String.valueOf(map1.get("publicPostId"))));
    postComment.setCommentContent(filter.replaceSensitiveWord(String.valueOf(map1.get("content")),1,"*"));
    postComment.setCommentTime(LocalDateTime.now());
    int flag = publicPostDao.publicpostComment(postComment);
    if(flag > 0){
      return true;
    }
    return false;
  }

  @Override
  public List<Emoji> findpublicostComment(Long publicpostid,Integer page,Integer size) {
    List<Emoji> emojis = publicPostDao.findpublicostComment(publicpostid,page,size);
    for(Emoji emoji:emojis){
      emoji.setReplyNums(publicPostDao.countReply(emoji.getCommentId()));
    }
    return emojis;
  }

  @Override
  public int countPublicostComment(Long publicpostid) {
    return publicPostDao.countPublicostComment(publicpostid);
  }

  @Override
  public Map<String, Object> reply(Map<String, Object> map) {
    Reply reply  = new Reply();
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    reply.setCommentId(Integer.parseInt(String.valueOf(map.get("commentId"))));
    reply.setReplycontent(filter.replaceSensitiveWord(String.valueOf(map.get("content")),0,"*"));
    reply.setFromuserId(accountService.findIdByAccountName(String.valueOf(map.get("fromuser"))));
    reply.setIsat(Integer.parseInt(String.valueOf(map.get("isat"))));
    reply.setTouserId(Long.parseLong(String.valueOf(map.get("touser"))));
    reply.setPublicpostid(Long.parseLong(String.valueOf(map.get("publicpostid"))));
    reply.setReplyTime(LocalDateTime.now());
    HashMap<String, Object> map1 = new HashMap<>();
    if(publicPostDao.reply(reply)!= 0){
      map1.put("state",true);
      map1.put("msg","回复成功");
    }else{
      map1.put("state",false);
      map1.put("msg","回复失败");
    }
//    commentId: 20
//    content: "<p>ccc</p>"
//    fromuser: "zycdxxgcda"
//    isat: 1
//    touser: 0
    return map1;
  }

  @Override
  public List<Reply> findreply(Long publicpostid,Integer page,Integer size) {
    List<Emoji> emojis = publicPostDao.findpublicostComment(publicpostid, page, size);
    System.out.println(emojis);
    List<Integer> commentIds = new ArrayList<>();
    for (Emoji emoji : emojis) {
      commentIds.add(emoji.getCommentId());
    }
    List<Reply> replies = new ArrayList<>();
    for(Integer comment:commentIds){
      replies.addAll(publicPostDao.findreply(comment));
    }
    for(Reply reply : replies) {
      reply.setFromusername(usersDao.findUsernameById(reply.getFromuserId()));
      reply.setTouusername(usersDao.findUsernameById(reply.getTouserId()));
    }
    return replies;
  }

  @Override
  public List<Reply> findReplyByCommentId(Long publicpostid, Integer commentId, Integer commentPage, Integer replyPage,Integer commentpagesize,Integer replypagesize) {
    List<Emoji> emojis = publicPostDao.findpublicostComment(publicpostid, commentPage, commentpagesize);
    System.out.println(emojis);
    List<Integer> commentIds = new ArrayList<>();
    for (Emoji emoji : emojis) {
      commentIds.add(emoji.getCommentId());
    }
    System.out.println(commentIds);
    List<Reply> replies = new ArrayList<>();
    for(Integer comment:commentIds){
      if(comment.equals(commentId)){
        replies.addAll(publicPostDao.findReplyByCommentId(commentId,replyPage,replypagesize));
      }else{
        replies.addAll(publicPostDao.findreply(comment));
      }
    }
    for(Reply reply : replies) {
      reply.setFromusername(usersDao.findUsernameById(reply.getFromuserId()));
      reply.setTouusername(usersDao.findUsernameById(reply.getTouserId()));
    }
    return replies;
  }

  @Override
  public Map<String, Object> getStatisticalInfo() {
    HashMap<String, Object> map = new HashMap<>();
    try {
      HashMap<String, Object> resultMap = new HashMap<>();
      resultMap.put("userNum",accountDao.getAllUserNumber());
      resultMap.put("postNumber",publicPostDao.getAllPostNumber());
      resultMap.put("replyNumber",publicPostDao.getAllReplyNumber()+ dynamicDao.getAllReplyNumber());
      resultMap.put("classNumber",classesDao.getAllClassNumber());
      resultMap.put("schoolNumber",schoolDao.getAllSchoolNumber());
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("allNum",resultMap);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询异常");
    }
    return map;
  }
}
