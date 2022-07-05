package com.zy.final_project.controller;

import com.zy.final_project.entity.School;
import com.zy.final_project.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/school")
public class SchoolController {

  @Autowired
  private SchoolService schoolService;
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
    File targetFile = new File("F:\\Project_two\\src\\main\\resources\\static\\schoolphoto", uploadFileName);// 目标文件
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
    map.put("url","http://localhost:8181/schoolphoto/"+uploadFileName);
    return map;
  }

  @PostMapping("/createSchool")
  public Map<String,Object> createSchool(@RequestBody School school){
    System.out.println(school);
    return schoolService.createSchool(school);
  }

  @GetMapping("/findAllSchool/{page}/{size}")
  public Map<String,Object> findAllSchool(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
    return schoolService.findAllSchool((page-1)*size,size);
  }

  @GetMapping("/findAllInSchool/{userId}/{page}/{size}")
  public Map<String,Object> findAllInSchool(@PathVariable("userId") Long userId,
                                            @PathVariable("page") Integer page,
                                            @PathVariable("size") Integer size){
    return schoolService.findAllInSchool(userId,(page-1)*size,size);
  }

// 用户输入数据后进行的模糊查询全部学校的名字 用于下拉框的回显
  @GetMapping("/searchSchoolForName/{name}")
  public Map<String,Object> searchSchoolForName(@PathVariable("name") String name){
    System.out.println(name);
    return schoolService.searchSchoolForName(name);
  }

  // 用户模糊查询已加入的学校名字 用于下拉框的回显
  @GetMapping("/searchSchoolForName/{userId}/{name}")
  public Map<String,Object> searchInSchoolForName(@PathVariable("userId") Long userId,
                                                @PathVariable("name") String name){
    System.out.println(name);
    return schoolService.searchInSchoolForName(userId,name);
  }

  @GetMapping("/searchSchool/{name}/{page}/{size}")
  public Map<String,Object> searchSchool(@PathVariable("name") String name,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return schoolService.searchSchool(name,(page-1)*size,size);
  }

  @GetMapping("/searchInSchool/{userId}/{name}/{page}/{size}")
  public Map<String,Object> searchInSchool(@PathVariable("userId") Long userId,
                                         @PathVariable("name") String name,
                                         @PathVariable("page") Integer page,
                                         @PathVariable("size") Integer size){
    return schoolService.searchInSchool(userId,name,(page-1)*size,size);
  }

  @GetMapping("/findSchoolInfo/{schoolId}/{page}/{size}")
  public Map<String,Object> findSchoolInfo(@PathVariable("schoolId") Long schoolId,
                                          @PathVariable("page") Integer page,
                                          @PathVariable("size") Integer size){
    return schoolService.findSchoolInfo(schoolId,(page-1)*size,size);
  }
}
