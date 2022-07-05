package com.zy.final_project.controller;

import com.zy.final_project.entity.dynamic.Dynamic;
import com.zy.final_project.entity.dynamic.DynamicComment;
import com.zy.final_project.entity.dynamic.DynamicLikes;
import com.zy.final_project.entity.dynamic.DynamicReply;
import com.zy.final_project.service.DynamicLikesService;
import com.zy.final_project.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dynamic")
public class DynamicController {
  @Autowired
  private DynamicService dynamicService;
  @PostMapping("/selectJpgUrl")
  public Map<String, String> selectJpgUrl(@RequestParam("images") MultipartFile mf, HttpServletRequest request)
          throws IOException {
    //拿到上传的文件名字+后缀名 例如 test.jpg
    //获取源文件
    String filename = mf.getOriginalFilename();
    System.out.println(filename);
    String[] names = filename.split("\\.");//
    for(String name:names){
      System.out.println(name);
    }
    String tempNum = (int) (Math.random() * 100000) + "";
    String uploadFileName = tempNum + System.currentTimeMillis() + "." + names[names.length - 1];
    File targetFile = new File("F:\\Project_two\\src\\main\\resources\\static\\dynamic", uploadFileName);// 目标文件
    // 开始从源文件拷贝到目标文件
    // 传图片一步到位
    try {
      mf.transferTo(targetFile);
    } catch (IllegalStateException e){
      e.printStackTrace();
    } catch (IOException e){
      e.printStackTrace();
    }
    HashMap<String, String> map = new HashMap<>();
    map.put("error","0");
    System.out.println(uploadFileName);
    map.put("data","http://localhost:8181/dynamic/"+uploadFileName);// 这里应该是项目路径
    map.put("url","http://localhost:8181/dynamic/"+uploadFileName);
    return map;
  }

  //发布动态接口
  @PostMapping("/postDynamic")
  public Map<String, Object> postDynamic(@RequestBody Map<String,Object> map){
    return dynamicService.postDynamic(map);
  }

  //查找动态接口
  @GetMapping("/findDynamic/{userId}/{page}/{size}")
  public Map<String, Object> findDynamic(@PathVariable("userId") Long userId,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return dynamicService.findDynamic(userId,(page-1)*size,size);
  }

  //查找我的动态接口
  @GetMapping("/findMyDynamic/{userId}/{page}/{size}")
  public Map<String, Object> findMyDynamic(@PathVariable("userId") Long userId,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return dynamicService.findMyDynamic(userId,(page-1)*size,size);
  }

  //动态中的评论接口
  @PostMapping("/postDynamicComment")
  public Map<String,Object> postDynamicComment(@RequestBody DynamicComment dynamicComment){
    return dynamicService.postDynamicComment(dynamicComment);
  }

  //动态中的查询评论接口 只显示6条评论的接口
  @GetMapping("/findDynamicComment/{dynamicId}")
  public Map<String,Object> findDynamicComment(@PathVariable("dynamicId") Long dynamicId){
    return dynamicService.findDynamicComment(dynamicId);
  }
  //动态中的查询评论接口 拿取评论 一次拿6条 load加载时使用的接口
  @GetMapping("/findDynamicComment/{dynamicId}/{page}/{size}")
  public Map<String,Object> findDynamicComment(@PathVariable("dynamicId") Long dynamicId,
                                               @PathVariable("page") Integer page,
                                               @PathVariable("size") Integer size){
    return dynamicService.findDynamicComment(dynamicId,(page-1)*size,size);
  }
  //查询单个动态内容
  @GetMapping("/findDynamic/{dynamicId}")
  public Map<String,Object> findDynamicByDynamicId(@PathVariable("dynamicId") Long dynamicId){
    System.out.println("动态ID："+dynamicId);
    return dynamicService.findDynamicByDynamicId(dynamicId);
  }
  //动态评论回复的接口
  @PostMapping("/postDynamicCommentReply")
  public Map<String,Object> postDynamicCommentReply(@RequestBody DynamicReply dynamicReply){
    return dynamicService.postDynamicCommentReply(dynamicReply);
  }
  //查找动态评论的回复接口   查找每条评论的前三条回复
  @GetMapping("/findDynamicReply/{dynamicId}")
  public Map<String,Object> findDynamicReply(@PathVariable("dynamicId") Long dynamicId){
    return dynamicService.findDynamicReply(dynamicId);
  }
  //查找动态评论的回复接口  拿取评论 一次拿6条 load加载时使用的接口
  @GetMapping("/findDynamicMoreReply/{dynamicId}/{page}/{size}")
  public Map<String,Object> findDynamicMoreReply(@PathVariable("dynamicId") Long dynamicId,
                                                 @PathVariable("page") Integer page,
                                                 @PathVariable("size") Integer size){
    return dynamicService.findDynamicMoreReply(dynamicId,(page-1)*size,size);
  }
  //查找动态评论的回复接口   查找一条评论下的前6条回复
  @GetMapping("/findDynamicReply/{dynamicId}/{commentId}/{page}/{size}")
  public Map<String,Object> findDynamicReply(@PathVariable("dynamicId") Long dynamicId,
                                             @PathVariable("commentId") Integer commentId,
                                             @PathVariable("page") Integer page,
                                             @PathVariable("size") Integer size){
    return dynamicService.findDynamicReply(dynamicId,commentId,(page-1)*size,size);
  }

  //动态转发接口
  @PostMapping("/postDynamicForward")
  public Map<String,Object> postDynamicForward(@RequestBody Dynamic dynamic){
    return dynamicService.postDynamicForward(dynamic);
  }

  //动态中的查询转发接口
  @GetMapping("/findDynamicForward/{dynamicId}/{page}/{size}")
  public Map<String,Object> findDynamicForward(@PathVariable("dynamicId") Long dynamicId,
                                               @PathVariable("page") Integer page,
                                               @PathVariable("size") Integer size){
    return dynamicService.findDynamicForward(dynamicId,(page-1)*size,size);
  }
}
