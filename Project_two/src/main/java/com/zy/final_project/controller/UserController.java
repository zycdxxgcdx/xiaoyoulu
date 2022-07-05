package com.zy.final_project.controller;

import com.zy.final_project.entity.Users;
import com.zy.final_project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UsersService usersService;
  @PostMapping("/profilePhoto")
  public Map<String, String> selectJpgUrl(@RequestParam("profilePhoto") MultipartFile mf)
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
    File targetFile = new File("F:\\Project_two\\src\\main\\resources\\static\\profilephoto", uploadFileName);// 目标文件
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
    System.out.println(uploadFileName);
    map.put("url","http://localhost:8181/profilephoto/"+uploadFileName);
    return map;
  }
  @GetMapping("/getUserProfile/{userId}")
  public Map<String, Object> getUserProfile(@PathVariable("userId") Long userId){
    return usersService.getUserProfile(userId);
  }

  @PostMapping("/updateUser")
  public Map<String, Object> updateUser(@RequestBody Users users){
    return usersService.updateUser(users);
  }

  @GetMapping("/userInfo/{userId}")
  public Map<String, Object> userInfo(@PathVariable("userId") Long userId){
    return usersService.userInfo(userId);
  }

  @GetMapping("/userPost/{userId}/{page}/{size}")
  public Map<String, Object> findAll(@PathVariable("userId") Long userId,@PathVariable("page") Integer page, @PathVariable("size") Integer size){
    System.out.println(userId);
    return usersService.findAll(userId,(page-1)*size,size);
  }

  @PostMapping("/follow/{userId}/{followUserId}")
  public Map<String, Object> follow(@PathVariable("userId") Long userId,
                                    @PathVariable("followUserId") Long followUserId){
    return usersService.follow(userId,followUserId);
  }

  @DeleteMapping("/cancelFollow/{userId}/{followUserId}")
  public Map<String, Object> cancelFollow(@PathVariable("userId") Long userId,
                                    @PathVariable("followUserId") Long followUserId){
    return usersService.cancelFollow(userId,followUserId);
  }

  @GetMapping("/isFollow/{userId}/{followUserId}")
  public Map<String, Object> isFollow(@PathVariable("userId") Long userId,
                                      @PathVariable("followUserId") Long followUserId){
    return usersService.isFollow(userId,followUserId);
  }

  @GetMapping("findFollowAndFansNums/{userId}")
  public Map<String, Object> findFollowAndFansNums(@PathVariable("userId") Long userId){
    return usersService.findFollowAndFansNums(userId);
  }

  @GetMapping("findFollows/{userId}/{page}/{size}")
  public Map<String, Object> findFollows(@PathVariable("userId") Long userId,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return usersService.findFollows(userId,(page-1)*size,size);
  }

  @GetMapping("findFans/{userId}/{page}/{size}")
  public Map<String, Object> findFans(@PathVariable("userId") Long userId,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return usersService.findFans(userId,(page-1)*size,size);
  }
}
