package com.zy.final_project.controller;

import com.zy.final_project.entity.classes.Classes;
import com.zy.final_project.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/classes")
public class ClassesController {

  @Autowired
  private ClassesService classesService;
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
//    Math.random()随机产生一个0到1之间的double类型随机值
    String tempNum = (int) (Math.random() * 100000) + "";
    String uploadFileName = tempNum + System.currentTimeMillis() + "." + names[names.length - 1];
    File targetFile = new File("F:\\Project_two\\src\\main\\resources\\static\\classesphoto", uploadFileName);// 目标文件
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
    map.put("url","http://localhost:8181/classesphoto/"+uploadFileName);
    return map;
  }

  @GetMapping("/getSchools/{province}/{city}")
  public Map<String,Object> findAllSchool(@PathVariable("province") String province,
                                          @PathVariable("city") String city){
    return classesService.findAllSchool(province,city);
  }
  @PostMapping("/createClass")
  public Map<String,Object> createClass(@RequestBody Classes classes){
    return classesService.createClass(classes);
  }

  @GetMapping("/findAllClasses/{page}/{size}")
  public Map<String,Object> findAllClasses(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
    return classesService.findAllClasses((page-1)*size,size);
  }

//  获取用户所在班级
  @GetMapping("/findInClasses/{userId}/{page}/{size}")
  public Map<String,Object> findInClasses(@PathVariable("userId") Long userId,
                                           @PathVariable("page") Integer page,
                                           @PathVariable("size") Integer size){
    return classesService.findInClasses(userId,(page-1)*size,size);
  }

  @GetMapping("/searchClass/{name}/{page}/{size}")
  public Map<String,Object> searchClass(@PathVariable("name") String name,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return classesService.searchClass(name,(page-1)*size,size);
  }

  @GetMapping("/searchInClass/{userId}/{name}/{page}/{size}")
  public Map<String,Object> searchInClass(@PathVariable("userId") Long userId,
                                        @PathVariable("name") String name,
                                        @PathVariable("page") Integer page,
                                        @PathVariable("size") Integer size){
    return classesService.searchInClass(userId,name,(page-1)*size,size);
  }

  @GetMapping("/findClassInfo/{classId}/{page}/{size}")
  public Map<String,Object> findClassInfo(@PathVariable("classId") Long classId,
                                          @PathVariable("page") Integer page,
                                          @PathVariable("size") Integer size){
    return classesService.findClassInfo(classId,(page-1)*(size),size);
  }

  //检查当前登录用户的身份
  @GetMapping("/checkMemberIdentity/{classId}/{userId}")
  public Map<String,Object> checkMemberIdentity(@PathVariable("classId") Long classId,
                                                @PathVariable("userId") Long userId){
    return classesService.checkMemberIdentity(classId,userId);
  }

  @DeleteMapping("/delClassMember/{userId}/{classId}")
  public Map<String,Object> delClassMember(@PathVariable("userId") Long userId,
                                           @PathVariable("classId") Long classId){
    return classesService.delClassMember(userId,classId);
  }

  @DeleteMapping("/exitClass/{userId}/{classId}")
  public Map<String,Object> exitClass(@PathVariable("userId") Long userId,
                                      @PathVariable("classId") Long classId){
    return classesService.exitClass(userId,classId);
  }

  @GetMapping("/findClassNameByClassId/{classId}")
  public Map<String,Object> findClassNameByClassId(@PathVariable("classId") Long classId){
    return classesService.findClassNameByClassId(classId);
  }
  @PostMapping("/joinClass/{userId}/{classId}")
  public Map<String,Object> joinClass(@PathVariable("userId") Long userId,
                                      @PathVariable("classId") Long classId){
    return classesService.joinClass(userId,classId);
  }

}
