package com.zy.final_project.service.impl;

import com.zy.final_project.dao.DynamicDao;
import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.dynamic.Dynamic;
import com.zy.final_project.entity.dynamic.DynamicComment;
import com.zy.final_project.entity.dynamic.DynamicReply;
import com.zy.final_project.service.DynamicLikesService;
import com.zy.final_project.service.DynamicService;
import com.zy.final_project.utils.SensitiveWordsUtils;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DynamicServiceimpl implements DynamicService {

  @Autowired
  private DynamicDao dynamicDao;
  @Autowired
  private UsersDao usersDao;
  @Autowired
  private DynamicLikesService dynamicLikesService;
  @Override
  public Map<String, Object> postDynamic(Map<String, Object> map) {
    HashMap<String, Object> map1 = new HashMap<>();
    Dynamic dynamic = new Dynamic();
    SnowFlakeUtils idWorker = new SnowFlakeUtils();
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    System.out.println(map);
    System.out.println((String)map.get("content"));
    System.out.println(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace((String)map.get("content")),1));
    if(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace((String)map.get("content")),1)){
      map1.put("state","false");
      map1.put("msg","动态内容含有屏蔽词");
      return map1;
    }
    dynamic.setDynamicId(idWorker.getId());
    dynamic.setUserId(Long.parseLong(String.valueOf(map.get("userId"))));
    dynamic.setContent((String)map.get("content"));
    dynamic.setSubmit_images((String)map.get("submitList"));
    dynamic.setDynamic_time(LocalDateTime.now());
    try{
      if(dynamicDao.postDynamic(dynamic) > 0){
        map1.put("state",true);
        map1.put("msg","发送动态成功");
      }else{
        map1.put("state",false);
        map1.put("msg","发送动态失败");
      }
    }catch (Exception e){
      e.printStackTrace();
      map1.put("state",false);
      map1.put("msg","发送动态发生异常");
    }
    return map1;
  }

  @Override
  public Map<String, Object> findDynamic(Long userId,Integer page,Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Dynamic> list = dynamicDao.findDynamic(userId,page,size);
      for(Dynamic item : list){
        item.setReplyNums(dynamicDao.countReplyByDynamicId(item.getDynamicId()));
        item.setForwardNums(dynamicDao.countForwardByDynamicId(item.getDynamicId()));
        if(dynamicLikesService.getLikedCountFromRedis(item.getDynamicId().toString()) == null){
          item.setLikesNums(0);
        }else{
          item.setLikesNums(dynamicLikesService.getLikedCountFromRedis(item.getDynamicId().toString()));
        }
        if(dynamicLikesService.checklike(userId.toString(),item.getDynamicId().toString())){
          item.setIslike(1);
        }else{
          item.setIslike(0);
        }
        if(item.getForwardDynamicId()!=null){
          item.setForwardDynamic(dynamicDao.findDynamicByDynamicId(item.getForwardDynamicId()));
        }
      }
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("dynamic",list);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findMyDynamic(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Dynamic> list = dynamicDao.findMyDynamic(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("dynamic",list);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> postDynamicReply(DynamicReply dynamicReply) {
    HashMap<String, Object> map = new HashMap<>();
    dynamicReply.setReply_time(LocalDateTime.now());
    try {
      if(dynamicDao.postDynamicReply(dynamicReply)>0){
        map.put("state",true);
        map.put("msg","发送回复成功");
      }else{
        map.put("state",false);
        map.put("msg","发送回复失败");
      }
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","发送回复异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicComment(Long dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      List<DynamicComment> list = dynamicDao.findDynamicComment(dynamicId);
      System.out.println(list);
      for(DynamicComment item : list){
        //计算时间差  返回分钟数
        item.setNums(dynamicDao.countReplyBycommentId(item.getCommentId()));
        Duration duration = Duration.between(item.getCommentTime(),now);
        System.out.println("分钟差："+duration.toMinutes());
        System.out.println("小时差"+duration.toHours());
        System.out.println("天数差"+duration.toDays());
        System.out.println("秒数差"+duration.toMillis()/1000);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询回复成功");
      map.put("comment",list);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询回复异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicComment(Long dynamicId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      List<DynamicComment> list = dynamicDao.findDynamicCommentPage(dynamicId,page,size);
      System.out.println(list);
      for(DynamicComment item : list){
        //计算时间差  返回分钟数
        item.setNums(dynamicDao.countReplyBycommentId(item.getCommentId()));
        Duration duration = Duration.between(item.getCommentTime(),now);
        System.out.println("分钟差："+duration.toMinutes());
        System.out.println("小时差"+duration.toHours());
        System.out.println("天数差"+duration.toDays());
        System.out.println("秒数差"+duration.toMillis()/1000);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询回复成功");
      map.put("comment",list);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询回复异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicByDynamicId(Long dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      Dynamic dynamic = dynamicDao.findDynamicByDynamicId(dynamicId);
      dynamic.setReplyNums(dynamicDao.countReplyByDynamicId(dynamicId));
      dynamic.setForwardNums(dynamicDao.countForwardByDynamicId(dynamicId));
      if(dynamicLikesService.getLikedCountFromRedis(dynamic.getDynamicId().toString()) == null){
        dynamic.setLikesNums(0);
      }else{
        dynamic.setLikesNums(dynamicLikesService.getLikedCountFromRedis(dynamic.getDynamicId().toString()));
      }
      if(dynamicLikesService.checklike(dynamic.getUserId().toString(),dynamic.getDynamicId().toString())){
        dynamic.setIslike(1);
      }else{
        dynamic.setIslike(0);
      }
      map.put("state",true);
      map.put("msg","查询动态成功");
      map.put("dynamic",dynamic);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询动态异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicReply(Long dynamicId) {
    HashMap<String, Object> map = new HashMap<>();
    List<DynamicComment> list = dynamicDao.findDynamicComment(dynamicId);
    for(DynamicComment dynamicComment:list){
      System.out.println(dynamicComment);
    }
    System.out.println(list);
    List<DynamicReply> replies = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      for(DynamicComment dynamicComment:list){
        List<DynamicReply> listreply = dynamicDao.findDynamicReplyByCommentId(dynamicComment.getCommentId());
        System.out.println(listreply);
        replies.addAll(listreply);
      }
      for(DynamicReply item:replies){
        item.setFromusername(usersDao.findUsernameById(item.getFromuserId()));
        item.setTouusername(usersDao.findUsernameById(item.getTouserId()));
        item.setHeadPosition(usersDao.userInfo(item.getFromuserId()).getHeadPosition());
        item.setNums(dynamicDao.countReplyBycommentId(item.getCommentId()));
        Duration duration = Duration.between(item.getReply_time(),now);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询回复成功");
      map.put("reply",replies);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询回复异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicMoreReply(Long dynamicId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    List<DynamicComment> list = dynamicDao.findDynamicCommentPage(dynamicId,page,size);
    for(DynamicComment dynamicComment:list){
      System.out.println(dynamicComment);
    }
    System.out.println(list);
    List<DynamicReply> replies = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      for(DynamicComment dynamicComment:list){
        List<DynamicReply> listreply = dynamicDao.findDynamicReplyByCommentId(dynamicComment.getCommentId());
        System.out.println(listreply);
        replies.addAll(listreply);
      }
      for(DynamicReply item:replies){
        item.setFromusername(usersDao.findUsernameById(item.getFromuserId()));
        item.setTouusername(usersDao.findUsernameById(item.getTouserId()));
        item.setHeadPosition(usersDao.userInfo(item.getFromuserId()).getHeadPosition());
        item.setNums(dynamicDao.countReplyBycommentId(item.getCommentId()));
        Duration duration = Duration.between(item.getReply_time(),now);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询回复成功");
      map.put("reply",replies);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询回复异常");
    }
    return map;
  }

  //动态评论接口
  @Override
  public Map<String, Object> postDynamicComment(DynamicComment dynamicComment) {
    HashMap<String, Object> map = new HashMap<>();
    dynamicComment.setCommentTime(LocalDateTime.now());
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    try{
      dynamicComment.setContent(filter.replaceSensitiveWord(StringUtils.deleteWhitespace(dynamicComment.getContent()),1,"*"));
      if(dynamicDao.postDynamicComment(dynamicComment)>0){
        map.put("state",true);
        map.put("msg","评论发送成功");
      }else{
        map.put("state",false);
        map.put("msg","评论发送失败");
      }
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",true);
      map.put("msg","评论发送异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> postDynamicCommentReply(DynamicReply dynamicReply) {
    System.out.println(dynamicReply);
    HashMap<String, Object> map = new HashMap<>();
    if(dynamicReply.getIsat() == 0){
      dynamicReply.setTouserId(dynamicDao.findUserIdByCommentId(dynamicReply.getCommentId()));
    }
    dynamicReply.setReply_time(LocalDateTime.now());
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    dynamicReply.setContent(filter.replaceSensitiveWord(StringUtils.deleteWhitespace(dynamicReply.getContent()),1,"*"));
    try{
      if(dynamicDao.postDynamicCommentReply(dynamicReply)>0){
        map.put("state",true);
        map.put("msg","回复发送成功");
      }else{
        map.put("state",false);
        map.put("msg","回复发送失败");
      }
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",true);
      map.put("msg","回复发送异常");
    }
    return map;
  }

  @Override
  public Map<String, Object>  findDynamicReply(Long dynamicId, Integer commentId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
//    List<DynamicComment> list = dynamicDao.findDynamicComment(dynamicId);
    List<DynamicComment> list = dynamicDao.findDynamicCommentPage(dynamicId,0,dynamicDao.rankCommentInDynamic(dynamicId));
    List<DynamicReply> replies = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      for(DynamicComment dynamicComment:list){
        if(dynamicComment.getCommentId() == commentId){
          List<DynamicReply> listreply = dynamicDao.findDynamicAllReplyByCommentId(dynamicComment.getCommentId(),page,size);
          replies.addAll(listreply);
        }else{
          List<DynamicReply> listreply = dynamicDao.findDynamicReplyByCommentId(dynamicComment.getCommentId());
          replies.addAll(listreply);
        }
      }
      for(DynamicReply item:replies){
        item.setFromusername(usersDao.findUsernameById(item.getFromuserId()));
        item.setTouusername(usersDao.findUsernameById(item.getTouserId()));
        item.setHeadPosition(usersDao.userInfo(item.getFromuserId()).getHeadPosition());
        item.setNums(dynamicDao.countReplyBycommentId(item.getCommentId()));
        Duration duration = Duration.between(item.getReply_time(),now);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询回复成功");
      map.put("reply",replies);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询回复异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> postDynamicForward(Dynamic dynamic) {
    HashMap<String, Object> map = new HashMap<>();
    SnowFlakeUtils snowFlakeUtils = new SnowFlakeUtils();
    dynamic.setDynamicId(snowFlakeUtils.getId());
    dynamic.setDynamic_time(LocalDateTime.now());
    if(dynamicDao.getForwardDynamicId(dynamic.getForwardDynamicId())!=null){
      dynamic.setForwardDynamicId(dynamicDao.getForwardDynamicId(dynamic.getForwardDynamicId()));
    }
    try{
      if(dynamicDao.postDynamicForward(dynamic)>0){
        map.put("state",true);
        map.put("msg","转发成功");
      }else{
        map.put("state",false);
        map.put("msg","转发成功失败");
      }
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",true);
      map.put("msg","转发成功异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicForward(Long dynamicId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();
    try {
      List<Dynamic> list = dynamicDao.findDynamicForward(dynamicId,page,size);
      System.out.println(list);
      for(Dynamic item : list){
        //计算时间差  返回分钟数
        Duration duration = Duration.between(item.getDynamic_time(),now);
        System.out.println("分钟差："+duration.toMinutes());
        System.out.println("小时差"+duration.toHours());
        System.out.println("天数差"+duration.toDays());
        System.out.println("秒数差"+duration.toMillis()/1000);
        if(duration.toDays()>0){
          //天数
          item.setTimeDifference(duration.toDays());
          item.setTimeDifferenceType("day");
        } else if(duration.toHours()>0){
          item.setTimeDifference(duration.toHours());
          item.setTimeDifferenceType("hour");
        } else if(duration.toMinutes()>0){
          item.setTimeDifference(duration.toMinutes());
          item.setTimeDifferenceType("minute");
        } else {
          item.setTimeDifference(duration.toMillis()/1000);
          item.setTimeDifferenceType("second");
        }
      }
      map.put("state",true);
      map.put("msg","查询转发成功");
      map.put("forwardInfo",list);
    }catch (Exception e){
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询转发异常");
    }
    return map;
  }
}
