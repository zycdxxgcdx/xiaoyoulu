package com.zy.final_project.controller;

import com.zy.final_project.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {
  @Autowired
  private MessageService messageService;

  @GetMapping("/findPostInfo/{userId}/{page}/{size}")
  public Map<String, Object> findPostInfo(@PathVariable("userId") Long userId,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return messageService.findPostInfo(userId,(page-1)*size,size);
  }

  @GetMapping("/findDynamicInfo/{userId}/{page}/{size}")
  public Map<String, Object> findDynamicInfo(@PathVariable("userId") Long userId,
                                          @PathVariable("page") Integer page,
                                          @PathVariable("size") Integer size){
    return messageService.findDynamicInfo(userId,(page-1)*size,size);
  }

  @GetMapping("/findReplyInfo/{userId}/{page}/{size}")
  public Map<String, Object> findReplyInfo(@PathVariable("userId") Long userId,
                                             @PathVariable("page") Integer page,
                                             @PathVariable("size") Integer size){
    return messageService.findReplyInfo(userId,(page-1)*size,size);
  }

  @GetMapping("/findClassApply/{userId}/{page}/{size}")
  public Map<String, Object> findClassApply(@PathVariable("userId") Long userId,
                                           @PathVariable("page") Integer page,
                                           @PathVariable("size") Integer size){
    return messageService.findClassApply(userId,(page-1)*size,size);
  }

  @PutMapping("/classApplyAgree/{Id}")
  public Map<String, Object> classApplyAgree(@PathVariable("Id") Integer Id){
    System.out.println(Id);
    return messageService.classApplyAgree(Id);
  }
  @PutMapping("/classApplyReject/{Id}")
  public Map<String, Object> classApplyReject(@PathVariable("Id") Integer Id){
    return messageService.classApplyReject(Id);
  }
  @GetMapping("/findClassApplyOne/{Id}")
  public Map<String, Object> findClassApplyOne(@PathVariable("Id") Integer Id){
    return messageService.findClassApplyOne(Id);
  }

  @GetMapping("/findClassNotify/{userId}/{page}/{size}")
  public Map<String, Object> findClassNotify(@PathVariable("userId") Long userId,
                                             @PathVariable("page") Integer page,
                                             @PathVariable("size") Integer size){
    return messageService.findClassNotify(userId,(page-1)*size,size);
  }

  @GetMapping("/findLikeInfo/{userId}/{page}/{size}")
  public Map<String, Object> findLikeInfo(@PathVariable("userId") Long userId,
                                             @PathVariable("page") Integer page,
                                             @PathVariable("size") Integer size){
    return messageService.findLikeInfo(userId,(page-1)*size,size);
  }
}
