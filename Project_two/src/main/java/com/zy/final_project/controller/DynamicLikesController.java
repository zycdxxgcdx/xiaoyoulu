package com.zy.final_project.controller;


import com.zy.final_project.entity.dynamic.DynamicLikes;
import com.zy.final_project.service.DynamicLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamiclikes")
public class DynamicLikesController {

  @Autowired
  private DynamicLikesService dynamicLikesService;
  //点赞
  @PostMapping("/likes/{userId}/{dynamicId}")
  public Map<String,Object> likes(@PathVariable("userId") String userId,
                                  @PathVariable("dynamicId") String dynamicId){
    return dynamicLikesService.saveLiked2Redis(userId,dynamicId);
  }
//  @PostMapping("/likes")
//  public Map<String,Object> likes(@RequestBody DynamicLikes dynamicLikes){
//    return dynamicLikesService.saveLiked2Redis(dynamicLikes);
//  }
//  @PostMapping("/deletelikes/{userId}/{dynamicId}")
//  public Map<String,Object> deletelikes(@PathVariable("userId") String userId,
//                                        @PathVariable("dynamicId") String dynamicId){
//    return dynamicLikesService.deleteLikedFromRedis(userId,dynamicId);
//  }
  //查看点赞对象
//  @GetMapping("/getAllLikes/{dynamicId}")
//  public List<DynamicLikes> getAllLikes(@PathVariable("dynamicId") String dynamicId){
//    return dynamicLikesService.getLikedDataFromRedis(dynamicId);
//  }
  //取消点赞
  @PostMapping("/dislikes/{userId}/{dynamicId}")
  public Map<String,Object> dislikes(@PathVariable("userId") String userId,
                                  @PathVariable("dynamicId") String dynamicId){
    return dynamicLikesService.unlikeFromRedis(userId,dynamicId);
  }
  //查看点赞数
  @GetMapping("/getLikes/{dynamicId}")
  public Integer likes(@PathVariable("dynamicId") String dynamicId){
    return dynamicLikesService.getLikedCountFromRedis(dynamicId);
  }
}
