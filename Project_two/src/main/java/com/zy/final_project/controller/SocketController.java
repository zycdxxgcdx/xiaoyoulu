package com.zy.final_project.controller;

import com.zy.final_project.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/socket")
public class SocketController {

  @Autowired
  private SocketService socketService;
  @GetMapping("/findSocketList/{userId}/{page}/{size}")
  public Map<String,Object> findSocketList(@PathVariable("userId") Long userId,
                                           @PathVariable("page") Integer page,
                                           @PathVariable("size") Integer size){
    return socketService.findSocketList(userId,(page-1)*size,size);
  }

  @PutMapping("/delListUser/{fromUserId}/{toUserId}")
  public Map<String,Object> delListUser(@PathVariable("fromUserId") Long fromUserId,
                                        @PathVariable("toUserId") Long toUserId){
    return socketService.delListUser(fromUserId,toUserId);
  }

  @GetMapping("/findSocketMsg/{fromUserId}/{toUserId}/{page}/{size}")
  public Map<String,Object> findSocketMsg(@PathVariable("fromUserId") Long fromUserId,
                                          @PathVariable("toUserId") Long toUserId,
                                          @PathVariable("page") Integer page,
                                          @PathVariable("size") Integer size){
    return socketService.findSocketMsg(fromUserId,toUserId,(page-1)*size,size);
  }

  @PostMapping("addSocketList/{fromUserId}/{toUserId}/{type}")
//  type: 0单聊 1群聊
  public Map<String,Object> addSocketList(@PathVariable("fromUserId") Long fromUserId,
                                          @PathVariable("toUserId") Long toUserId,
                                          @PathVariable("type") Integer type){
    return socketService.addSocketList(fromUserId,toUserId,type);
  }

  @GetMapping("/findSocketGroupMsg/{toUserId}/{page}/{size}")
  public Map<String,Object> findSocketGroupMsg(@PathVariable("toUserId") Long toUserId,
                                               @PathVariable("page") Integer page,
                                               @PathVariable("size") Integer size){
    return socketService.findSocketGroupMsg(toUserId,(page-1)*size,size);
  }
}
