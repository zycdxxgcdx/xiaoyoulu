package com.zy.final_project.service.impl;

import com.zy.final_project.dao.ClassesUsersDao;
import com.zy.final_project.dao.MessageDao;
import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.Users;
import com.zy.final_project.entity.classes.ClassesUsers;
import com.zy.final_project.entity.dynamic.DynamicLikes;
import com.zy.final_project.entity.message.MessageClass;
import com.zy.final_project.entity.message.MessageExitClass;
import com.zy.final_project.entity.message.MessageLike;
import com.zy.final_project.entity.message.MessageReply;
import com.zy.final_project.service.DynamicLikesService;
import com.zy.final_project.service.MessageService;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MessageServiceimpl implements MessageService {

  @Autowired
  private MessageDao messageDao;
  @Autowired
  private ClassesUsersDao classesUsersDao;
  @Autowired
  private DynamicLikesService dynamicLikesService;
  @Autowired
  private UsersDao usersDao;
  @Override
  public Map<String, Object> findPostInfo(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<MessageReply> message = messageDao.findPostInfo(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messagePost",message);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findDynamicInfo(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<MessageReply> message = messageDao.findDynamicInfo(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageDynamic",message);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findReplyInfo(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<MessageReply> message = messageDao.findReplyInfo(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageReply",message);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findClassApply(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<MessageClass> message = messageDao.findClassApply(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageClassApply",message);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> classApplyAgree(Integer Id) {
    HashMap<String, Object> map = new HashMap<>();
    ClassesUsers classesUsers = new ClassesUsers();
    SnowFlakeUtils snowFlakeUtils = new SnowFlakeUtils();
    try {
     if(messageDao.classApplyAgree(Id, LocalDateTime.now()) > 0){
       MessageClass messageClass = messageDao.findMessageClassById(Id);
       classesUsers.setId(snowFlakeUtils.getId());
       classesUsers.setClassId(messageClass.getClassId());
       classesUsers.setUserId(messageClass.getUserId());
       classesUsers.setRole(0);
       classesUsers.setInClassTime(new Date());
       classesUsersDao.createInfo(classesUsers);
       map.put("state",true);
       map.put("msg","同意成功");
     }else {
       map.put("state",false);
       map.put("msg","同意失败");
     }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","同意发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findClassApplyOne(Integer Id) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      MessageClass message = messageDao.findMessageClassById(Id);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageClassApply",message);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> classApplyReject(Integer Id) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(messageDao.classApplyReject(Id, LocalDateTime.now()) > 0){
        map.put("state",true);
        map.put("msg","拒绝成功");
      }else {
        map.put("state",false);
        map.put("msg","拒绝失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","拒绝发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findClassNotify(Long userId,Integer page,Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<MessageExitClass> messageExitClasses = messageDao.findClassNotify(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageClassNotify",messageExitClasses);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findLikeInfo(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<DynamicLikes> dynamicLikes = dynamicLikesService.getLikedDataFromRedis(userId);
//      System.out.println(dynamicLikes);
      int n =0;
      for(DynamicLikes item:dynamicLikes){
        System.out.println("item"+(n++)+":::::"+item.getDynamicId());
      }
      Users users;
      List<MessageLike> messageLikes = messageDao.findLikeInfo(dynamicLikes,userId,page,size);
      System.out.println(messageLikes);
      System.out.println(messageLikes.size());
      for(DynamicLikes item:dynamicLikes){
        for(MessageLike item1:messageLikes){
          if(item1.getDynamicId() == Long.parseLong(item.getDynamicId())){
            users = usersDao.userInfo(Long.parseLong(item.getUserId()));
            item1.setUserId(Long.parseLong(item.getUserId()));
            item1.setUsername(users.getUsername());
            item1.setHeadPosition(users.getHeadPosition());
            break;
          }
        }
      }
      System.out.println(messageLikes);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("messageLike",messageLikes);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }
}
