package com.zy.final_project.controller;

import com.zy.final_project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

  @Autowired
  private PostService postService;
  @PostMapping("/selectJpgUrl")
  public Map<String, String> selectJpgUrl(@RequestParam("images") MultipartFile mf, HttpServletRequest request)
          throws IOException {
    String realPath = request.getSession().getServletContext().getRealPath("images");
    System.out.println(realPath);
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
    File targetFile = new File("F:\\Project_two\\src\\main\\resources\\static\\images", uploadFileName);// 目标文件
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
    map.put("data","http://localhost:8181/images/"+uploadFileName);// 这里应该是项目路径
    map.put("url","http://localhost:8181/images/"+uploadFileName);
    return map;
  }

  @PostMapping("/publicPost")
  public Map<String, String> publicPost(@RequestBody Map<String, Object> map){
//    HashMap<String, String> hashMap = new HashMap<>();
//    hashMap.put("msg","发帖成功");
    return postService.publicPost(map);
  }

  @GetMapping("/getMySchool/{userId}")
  public Map<String, Object> getMySchool(@PathVariable("userId") Long userId){
    return postService.getMySchool(userId);
  }
}
