package com.zy.final_project.service.impl;

import com.zy.final_project.dao.*;
import com.zy.final_project.entity.socket.SocketList;
import com.zy.final_project.entity.socket.SocketMsg;
import com.zy.final_project.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SocketServiceimpl implements SocketService {
  @Autowired
  private SocketListDao socketListDao;
  @Autowired
  private SocketMsgDao socketMsgDao;
  @Autowired
  private UsersDao usersDao;
  @Autowired
  private ClassesDao classesDao;
  @Autowired
  private ClassesUsersDao classesUsersDao;

  @Override
  public boolean checkTimeDifference(SocketMsg socketMsg,Integer index) {
    try {
      Duration duration = null;
      if(index == 0){
        duration = Duration.between(socketMsgDao.checkTimeDifference(socketMsg.getFromUser()),socketMsg.getTime());
      }else if(index == 1){
        duration = Duration.between(socketMsgDao.checkGroupTimeDifference(socketMsg.getFromUser()),socketMsg.getTime());
      }
      System.out.println("++++++++++");
      System.out.println(duration.toMinutes());
      System.out.println("++++++++++");
      if(duration.toMinutes()>2){
        return true;
      }else{
        return false;
      }
    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean addPrivateMsg(SocketMsg socketMsg) {
    try {
      if(this.checkTimeDifference(socketMsg,0)){
        this.addSocketList(socketMsg.getFromUser(),socketMsg.getToUser(),0);
        socketMsg.setIslast(1);
        socketMsgDao.addPrivateMsg(socketMsg);
        return true;
      }else{
        this.addSocketList(socketMsg.getFromUser(),socketMsg.getToUser(),0);
        socketMsg.setIslast(0);
        socketMsgDao.addPrivateMsg(socketMsg);
        return true;
      }
    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean addGroupMsg(SocketMsg socketMsg) {
    try {
      if(this.checkTimeDifference(socketMsg,1)){
        System.out.println("群聊");
        this.addSocketList(socketMsg.getFromUser(),socketMsg.getToUser(),1);
        socketMsg.setIslast(1);
        socketMsgDao.addGroupMsg(socketMsg);
        return true;
      }else{
        this.addSocketList(socketMsg.getFromUser(),socketMsg.getToUser(),1);
        socketMsg.setIslast(0);
        socketMsgDao.addGroupMsg(socketMsg);
        return true;
      }
    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Map<String, Object> findSocketList(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<SocketList> list = socketListDao.findSocketList(userId,page,size);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("SocketList",list);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> delListUser(Long fromUserId, Long toUserId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(socketListDao.delListUser(fromUserId,toUserId, LocalDateTime.now())>0){
        map.put("state",true);
        map.put("msg","删除用户列表成功");
      }else{
        map.put("state",false);
        map.put("msg","删除用户列表失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","删除用户列表发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findSocketMsg(Long fromUserId, Long toUserId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<SocketMsg> list = socketMsgDao.findSocketMsg(fromUserId,toUserId,page,size);
      map.put("state",true);
      map.put("msg","查询消息记录成功");
      map.put("socketMsg",list);
      map.put("toUsername",usersDao.findUsernameById(toUserId));
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询消息记录发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> addSocketList(Long fromUserId, Long toUserId, Integer type) {
    System.out.println("fromUserId:"+fromUserId);
    System.out.println("toUserId:"+toUserId);
    System.out.println("type:"+type);
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(socketListDao.checkSocketListExist(fromUserId,toUserId) == null){
        if(type == 0){
          //单聊
          if(socketListDao.addSocketList(fromUserId,toUserId,LocalDateTime.now(),type) > 0){
            //更新接收方的聊天列表
            if(socketListDao.checkSocketListExist(toUserId,fromUserId) == null){
              socketListDao.addSocketList(toUserId,fromUserId,LocalDateTime.now(),type);
            }else {
              socketListDao.updateSocketListState(toUserId,fromUserId,LocalDateTime.now(),type);
            }
            map.put("state",true);
            map.put("msg","更新消息列表成功");
          }else{
            map.put("state",false);
            map.put("msg","更新消息列表失败");
          }
        }else if(type == 1){
          //群聊
          //拿到班级里所有成员的ID  将班级中每一个成员的聊天列表都添加进来
          List<Long> classIds = classesUsersDao.findUserIdByClassId(toUserId);
          System.out.println("classsIds:"+classIds);
          for(Long item:classIds){
            if(socketListDao.checkSocketListExist(item,toUserId) == null){
              socketListDao.addSocketList(item,toUserId,LocalDateTime.now(),type);
            }else {
              socketListDao.updateSocketListState(item,toUserId,LocalDateTime.now(),type);
            }
          }
            map.put("state",true);
            map.put("msg","更新消息列表成功");
        }else {
          //传入的type不是0或者1的情况
          map.put("state",false);
          map.put("msg","更新消息列表失败");
        }
      }else if(socketListDao.checkSocketListExist(fromUserId,toUserId) > 0){
        System.out.println("群聊2");
        //当前用户聊天列表存在
        if(type == 0){
          //单聊
            socketListDao.updateSocketListState(fromUserId,toUserId,LocalDateTime.now(),type);
            //更新接收方的聊天列表
            if(socketListDao.checkSocketListExist(toUserId,fromUserId) == null){
              socketListDao.addSocketList(toUserId,fromUserId,LocalDateTime.now(),type);
            }else {
              socketListDao.updateSocketListState(toUserId,fromUserId,LocalDateTime.now(),type);
            }
            map.put("state",true);
            map.put("msg","更新消息列表成功");
          } else if(type == 1) {
          //群聊
          //拿到班级里所有成员的ID  将班级中每一个成员的聊天列表都添加进来
          System.out.println("群聊3");
          List<Long> classIds = classesUsersDao.findUserIdByClassId(toUserId);
          System.out.println("classIds:" + classIds);
          for (Long item : classIds) {
            if (socketListDao.checkSocketListExist(item, toUserId) == null) {
              socketListDao.addSocketList(item, toUserId, LocalDateTime.now(), type);
            } else {
              socketListDao.updateSocketListState(item, toUserId, LocalDateTime.now(), type);
            }
          }
          map.put("state", true);
          map.put("msg", "更新消息列表成功");
        }
      }else{
        map.put("state",false);
        map.put("msg","更新消息列表失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","更新消息列表发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findSocketGroupMsg(Long toUserId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<SocketMsg> list = socketMsgDao.findSocketGroupMsg(toUserId,page,size);
      map.put("state",true);
      map.put("msg","查询消息记录成功");
      map.put("socketMsg",list);
      map.put("toUsername",classesDao.findClassNameByClassId(toUserId));
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询消息记录发生异常");
    }
    return map;
  }
}
