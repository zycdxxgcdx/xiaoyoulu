package com.zy.final_project.service.impl;

import com.zy.final_project.dao.ClassesDao;
import com.zy.final_project.dao.ClassesUsersDao;
import com.zy.final_project.dao.SchoolDao;
import com.zy.final_project.entity.classes.Classes;
import com.zy.final_project.entity.School;
import com.zy.final_project.entity.Users;
import com.zy.final_project.service.SchoolService;
import com.zy.final_project.utils.SensitiveWordsUtils;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SchoolServiceimpl implements SchoolService {

  @Autowired
  private SchoolDao schoolDao;
  @Autowired
  private ClassesDao classesDao;
  @Autowired
  private ClassesUsersDao classesUsersDao;
  @Override
  public Map<String, Object> createSchool(School school) {
    HashMap<String, Object> map = new HashMap<>();
    SensitiveWordsUtils filter = SensitiveWordsUtils.getInstance();
    try {
      if(schoolDao.checkSchoolIsCreated(school) == null){
        if(filter.checkContainSensitiveWord(StringUtils.deleteWhitespace(school.getSchoolName()),1)){
          map.put("state","false");
          map.put("msg","学校名含有屏蔽词");
          return map;
        }
        SnowFlakeUtils idWorker = new SnowFlakeUtils();
        school.setSchoolId(idWorker.getId());
        schoolDao.createSchool(school);
        map.put("state",true);
        map.put("msg","创建成功");
      }else{
        map.put("state",false);
        map.put("msg","已创建");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","创建发生异常");
    }
    return map;
  }


  @Override
  public Map<String, Object> findAllSchool(Integer page,Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<School> list = schoolDao.findAllSchool(page,size);
      Integer allSchools = schoolDao.countAllSchool();
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",list);
      map.put("allSchoolsNums",allSchools);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findAllInSchool(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<School> list = schoolDao.findAllInSchool(userId,page,size);
      Integer allSchools = schoolDao.countAllInSchool(userId);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",list);
      map.put("allSchoolsNums",allSchools);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchSchoolForName(String name) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<String> list = schoolDao.searchSchoolForName(name);
      List<Map<String,String>> lists = new ArrayList<>();
      for(String item:list){
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("value",item);
        lists.add(map1);
      }
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",lists);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchInSchoolForName(Long userId, String name) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<String> list = schoolDao.searchInSchoolForName(userId,name);
      List<Map<String,String>> lists = new ArrayList<>();
      for(String item:list){
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("value",item);
        lists.add(map1);
      }
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",lists);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchSchool(String name,Integer page,Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<School> list = schoolDao.searchSchool(name,page,size);
      Integer allSchools = schoolDao.countAllSchoolForFuzzySearch(name);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",list);
      map.put("allSchoolsNums",allSchools);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchInSchool(Long userId, String name, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<School> list = schoolDao.searchInSchool(userId,name,page,size);
      Integer allSchools = schoolDao.countInSchoolForFuzzySearch(userId,name);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",list);
      map.put("allSchoolsNums",allSchools);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }


  @Override
  public Map<String, Object> findSchoolInfo(Long schoolId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    List<Map<String,Object>> resultList = new ArrayList<>();
    try {
      List<Classes> Classes = classesDao.findClassNameBySchoolId(schoolId,page,size);
      for(Classes item:Classes){
        HashMap<String, Object> result = new HashMap<>();
        List<Users> users = classesUsersDao.findUserInfoByClassId(item.getClassId());
        Integer numbers = classesUsersDao.countMemberClass(item.getClassId());
        result.put("classInfo",item);
        result.put("classAdmin",users);
        result.put("classNumbers",numbers);
        resultList.add(result);
      }
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schoolInfo",resultList);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }
}
