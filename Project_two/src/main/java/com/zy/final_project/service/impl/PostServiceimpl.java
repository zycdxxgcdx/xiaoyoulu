package com.zy.final_project.service.impl;

import com.zy.final_project.dao.AccountDao;
import com.zy.final_project.dao.PostDao;
import com.zy.final_project.entity.School;
import com.zy.final_project.entity.message.MessageExitClass;
import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.service.PostService;
import com.zy.final_project.utils.SensitiveWordsUtils;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PostServiceimpl implements PostService {

  @Autowired
  private AccountDao accountDao;
  @Autowired
  private PostDao postDao;
  @Override
  public Map<String, String> publicPost(Map<String, Object> map) {
    System.out.println(map);
    SnowFlakeUtils idWorker = new SnowFlakeUtils();
    PublicPost post = new PublicPost();
    HashMap<String, String> hashMap1 = new HashMap<>();
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap1 = (HashMap<String, String>)map.get("ruleForm");
    System.out.println(hashMap1);
    //屏蔽关键字 (标题,内容)
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    if(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace(hashMap1.get("title")),1)){
      hashMap.put("state","false");
      hashMap.put("msg","标题含有屏蔽词");
      return hashMap;
    }
    if(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace(hashMap1.get("content")),1)){
      hashMap.put("state","false");
      hashMap.put("msg","内容含有屏蔽词");
      return hashMap;
    }
    String title = filter.replaceSensitiveWord(StringUtils.deleteWhitespace(hashMap1.get("title")),1,"*");
    String content = filter.replaceSensitiveWord(StringUtils.deleteWhitespace(hashMap1.get("content")),1,"*");
    post.setSchool(hashMap1.get("school"));
    post.setPublicpostid(idWorker.getId());
    post.setTitle(title);
    post.setContent(content);
    post.setPostdate(LocalDateTime.now());
    post.setViews(0);
    post.setUserId(accountDao.findIdByAccountName((String)map.get("accountName")));
    post.setSubmit_images(hashMap1.get("submit_images"));
    System.out.println(post);

    String[] strings = hashMap1.get("delete_images").split(",");
    //删除用户上传了但是在输入框内又删除了的图片
    File file = null;
    if(strings != null){
      for(String string:strings){
        file = new File("F:\\Project_two\\src\\main\\resources\\static\\images\\"+string);
        if(file.exists()){
          file.delete();
          System.out.println("删除文件成功");
        }else{
          System.out.println("删除文件失败");
        }
      }
    }
    try {
      postDao.post(post);
      hashMap.put("state","true");
      hashMap.put("msg","发帖成功");
      return hashMap;
    }catch (Exception e){
      e.printStackTrace();
      hashMap.put("state","false");
      hashMap.put("msg","发帖失败");
      return hashMap;
    }
  }


  @Override
  public Map<String, Object> getMySchool(Long userId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      System.out.println(userId);
      List<School> schoolNames = postDao.getMySchool(userId);
      System.out.println(schoolNames);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schoolNames",schoolNames);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }
}
