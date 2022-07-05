package com.zy.final_project.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zy.final_project.dao.ClassesDao;
import com.zy.final_project.dao.SocketMsgDao;
import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.socket.SocketMsg;
import com.zy.final_project.service.SocketService;
import com.zy.final_project.utils.SensitiveWordsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{userId}/{toId}")
@Component
@Scope("prototype")
public class WebSocketService {
  private Long userId;
  private Session session;

  //用来存放每个客户端对应的MyWebSocket对象。
  private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<WebSocketService>();
  //与某个客户端的连接会话，需要通过它来给客户端发送数据
  //用来记录sessionId和该session进行绑定
  private static Map<Long, Session> map = new HashMap<>();
  private static ApplicationContext applicationContext;
  //注入dao
  private SocketMsgDao socketMsgDao;
  private SocketService socketService;
  private UsersDao usersDao;
  private ClassesDao classesDao;
  public static void setApplicationContext(ApplicationContext applicationContext) {
    WebSocketService.applicationContext = applicationContext;
  }

  /**
   * 连接建立成功调用的方法
   */
  @OnOpen
  public void onOpen(Session session, @PathParam("userId") Long userId,@PathParam("toId") Long toId) {
    Map<String,Object> message=new HashMap<String, Object>();
    this.session = session;
    this.userId = userId;
    map.put(userId, session);
    webSocketSet.add(this);//加入set中
    System.out.println("有新连接加入:" + userId + ",当前在线人数为" + webSocketSet.size());
    //this.session.getAsyncRemote().sendText("恭喜" + userId + "成功连接上WebSocket(其频道号：" + session.getId() + ")-->当前在线人数为：" + webSocketSet.size());
    message.put("type",0); //消息类型，0-连接成功，1-用户消息
    message.put("people",webSocketSet.size()); //在线人数
    message.put("name",userId); //昵称
    message.put("aisle",session.getId()); //频道号
    System.out.println(message);
    //返回给前端的信息  JSON字符串
    this.session.getAsyncRemote().sendText(new Gson().toJson(message));
  }

  /**
   * 连接关闭调用的方法    
   */
  @OnClose
  public void onClose() {
    webSocketSet.remove(this); //从set中删除
  }

  /**
   * 收到客户端消息后调用的方法
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session,
                        @PathParam("userId") Long userId,
                        @PathParam("toId") Long toId) {
//    System.out.println("来自客户端的消息-->" + userId + ": " + message);
//    System.out.println("接收端的消息-->" + toId + ": " + message);

    //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为SocketMsg对象，
    // 然后通过socketMsg的type进行判断是单聊还是群聊，进行相应的处理:
    ObjectMapper objectMapper = new ObjectMapper();
    System.out.println(message);
//    这里的message就是前端传过来的JSON字符串
    SocketMsg socketMsg;
    socketMsgDao = applicationContext.getBean(SocketMsgDao.class);
    usersDao = applicationContext.getBean(UsersDao.class);
    socketService = applicationContext.getBean(SocketService.class);
    classesDao = applicationContext.getBean(ClassesDao.class);
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    try {
      socketMsg = objectMapper.readValue(message, SocketMsg.class);
      socketMsg.setContent(filter.replaceSensitiveWord(StringUtils.deleteWhitespace(socketMsg.getContent()),1,"*"));
      socketMsg.setFromUser(userId);//发送者.
      socketMsg.setTime(LocalDateTime.now());
      if (socketMsg.getType() == 0) {
        //单聊.需要找到发送者和接受者.
        System.out.println(socketMsg);
        Session fromSession = map.get(userId);
        Session toSession = map.get(socketMsg.getToUser());
        Map<String,Object> m=new HashMap<>();
        //type是发送消息还是连接成功的指示
        m.put("type",1);
        //infotype 群聊还是私聊
        m.put("infotype",0);
        m.put("content",socketMsg.getContent());
        m.put("fromUser",socketMsg.getFromUser().toString());
        m.put("toUser",socketMsg.getToUser().toString());
        m.put("headPosition",usersDao.userInfo(userId).getHeadPosition());
        m.put("username",usersDao.userInfo(userId).getUsername());
        if(socketService.checkTimeDifference(socketMsg,0)){
          m.put("islast",1);
        }else{
          m.put("islast",0);
        }
        System.out.println(m);
        //发送给接受者.
        if (toSession != null) {
          if(socketService.addPrivateMsg(socketMsg)){
            fromSession.getAsyncRemote().sendText(new Gson().toJson(m));
            toSession.getAsyncRemote().sendText(new Gson().toJson(m));
          }
        } else {
          //发送给发送者.
          if(socketService.addPrivateMsg(socketMsg)){
            fromSession.getAsyncRemote().sendText(new Gson().toJson(m));
          }
        }
      } else {
        //群发消息
        Map<String,Object> m=new HashMap<>();
        //type是发送消息还是连接成功的指示
        m.put("type",1);
        m.put("infotype",1);
        m.put("content",socketMsg.getContent());
        m.put("fromUser",socketMsg.getFromUser().toString());
        m.put("toUser",socketMsg.getToUser().toString());
        m.put("headPosition",usersDao.userInfo(userId).getHeadPosition());
        m.put("username",usersDao.userInfo(userId).getUsername());
        m.put("schoolName",classesDao.findClassNameByClassId(socketMsg.getToUser()).getSchoolName());
        m.put("major",classesDao.findClassNameByClassId(socketMsg.getToUser()).getMajor());
        m.put("grade",classesDao.findClassNameByClassId(socketMsg.getToUser()).getGrade());
        m.put("className",classesDao.findClassNameByClassId(socketMsg.getToUser()).getClassName());
        if(socketService.checkTimeDifference(socketMsg,1)){
          m.put("islast",1);
        }else{
          m.put("islast",0);
        }
        if(socketService.addGroupMsg(socketMsg)){
          broadcast(m);
        }
      }

    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 发生错误时调用   
   */
  @OnError
  public void onError(Session session, Throwable error) {
    System.out.println("发生错误");
    error.printStackTrace();
  }

  /**
   * 群发自定义消息
   */
  public void broadcast(Map<String,Object> message) {
    for (WebSocketService item : webSocketSet) {
      item.session.getAsyncRemote().sendText(new Gson().toJson(message));//异步发送消息.
    }
  }
}
