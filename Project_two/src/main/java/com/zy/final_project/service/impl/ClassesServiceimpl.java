package com.zy.final_project.service.impl;

import com.zy.final_project.dao.ClassExitDao;
import com.zy.final_project.dao.ClassesDao;
import com.zy.final_project.dao.ClassesUsersDao;
import com.zy.final_project.dao.UsersDao;
import com.zy.final_project.entity.classes.Classes;
import com.zy.final_project.entity.classes.ClassesUsers;
import com.zy.final_project.entity.message.MessageExitClass;
import com.zy.final_project.service.ClassesService;
import com.zy.final_project.utils.SnowFlakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional  //事务注解
public class ClassesServiceimpl implements ClassesService {

  @Autowired
  private ClassesDao classesDao;
  @Autowired
  private ClassesUsersDao classesUsersDao;
  @Autowired
  private UsersDao usersDao;
  @Autowired
  private ClassExitDao classExitDao;
  @Override
  public Map<String, Object> findAllSchool(String province, String city) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<String> list = classesDao.findAllSchool(province,city);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("schools",list);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> createClass(Classes classes) {
    HashMap<String, Object> map = new HashMap<>();
    ClassesUsers classesUsers = new ClassesUsers();
    SnowFlakeUtils idWorker = new SnowFlakeUtils();
    classes.setClassId(idWorker.getId());
    classesUsers.setId(idWorker.getId());
    classesUsers.setClassId(classes.getClassId());
    classesUsers.setUserId(classes.getUserId());
    classesUsers.setRole(1);
    classesUsers.setInClassTime(new Date());
    try {
      classes.setSchoolId(classesDao.selectSchoolId(classes.getProvince(),classes.getCity(),classes.getSchoolName()));
      if(classesDao.checkClassIsCreated(classes) == null){
        if(classesDao.createClass(classes) > 0 && classesUsersDao.createInfo(classesUsers)>0){
          map.put("state",true);
          map.put("msg","创建成功");
        }else{
          map.put("state",false);
          map.put("msg","创建失败");
        }
      }else{
        map.put("state",false);
        map.put("msg","已存在");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findAllClasses(Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Classes> list = classesDao.findAllClasses(page,size);
      Integer allClass = classesDao.countAllClass();
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("classes",list);
      map.put("allClassNums",allClass);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findInClasses(Long userId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Classes> list = classesDao.findInClasses(userId,page,size);
      Integer allClass = classesDao.countInClass(userId);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("classes",list);
      map.put("allClassNums",allClass);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchClass(String name, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Classes> list = classesDao.searchClass(name,page,size);
      Integer allClass = classesDao.countsearchClass(name);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("classes",list);
      map.put("allClassNums",allClass);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> searchInClass(Long userId, String name, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<Classes> list = classesDao.searchInClass(userId,name,page,size);
      Integer allClass = classesDao.countsearchInClass(userId,name);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("classes",list);
      map.put("allClassNums",allClass);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findClassInfo(Long classId, Integer page, Integer size) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      List<ClassesUsers> list = classesUsersDao.findClassInfo(classId,page,size);
      Integer memberNumbers = classesUsersDao.countMemberClass(classId);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("userInfo",list);
      map.put("memberNumbers",memberNumbers);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> checkMemberIdentity(Long classId,Long userId) {
    HashMap<String, Object> map = new HashMap<>();

    try {
      ClassesUsers classesUsers = classesUsersDao.checkMemberIdentity(classId,userId);
      map.put("state",true);
      map.put("msg","查询成功");
      if (classesUsers == null) {
        map.put("isAdministrators",false);
        map.put("isMember",false);
      }else if(classesUsers.getRole() == 0){
        map.put("isAdministrators",false);
        map.put("isMember",true);
      }else if(classesUsers.getRole() == 1){
        map.put("isAdministrators",true);
        map.put("isMember",true);
      }else{
        map.put("state",false);
        map.put("msg","查询发生错误");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> delClassMember(Long userId, Long classId) {
    HashMap<String, Object> map = new HashMap<>();
    MessageExitClass messageExitClass = new MessageExitClass();
    try {
      Integer isDel = classesUsersDao.delClassMember(userId,classId);
      if(isDel > 0){
        messageExitClass.setClassId(classId);
        messageExitClass.setUserId(userId);
        // 1表示班级管理员删除用户
        messageExitClass.setExitType(1);
        messageExitClass.setTime(LocalDateTime.now());
        if(classExitDao.delByClass(messageExitClass)>0){
          map.put("state",true);
          map.put("msg","删除成功");
        }else{
          map.put("state",true);
          map.put("msg","删除失败");
        }
      }else{
        map.put("state",true);
        map.put("msg","删除失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","删除发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> exitClass(Long userId, Long classId) {
    HashMap<String, Object> map = new HashMap<>();
    MessageExitClass messageExitClass = new MessageExitClass();
    try {
      Integer isDel = classesUsersDao.exitClass(userId,classId);
      if(isDel > 0){
        messageExitClass.setClassId(classId);
        messageExitClass.setUserId(userId);
        //0表示用户自己退出班级
        messageExitClass.setExitType(0);
        messageExitClass.setTime(LocalDateTime.now());
        if(classExitDao.exitClass(messageExitClass)>0){
          map.put("state",true);
          map.put("msg","退出成功");
        }else{
          map.put("state",true);
          map.put("msg","退出失败");
        }
      }else{
        map.put("state",true);
        map.put("msg","退出失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","退出发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> findClassNameByClassId(Long classId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      Classes classes = classesDao.findClassNameByClassId(classId);
      map.put("state",true);
      map.put("msg","查询成功");
      map.put("classes",classes);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","查询发生异常");
    }
    return map;
  }

  @Override
  public Map<String, Object> joinClass(Long userId, Long classId) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      if(classesDao.checkIsJoinClass(userId,classId) == null){
        if(classesDao.joinClass(userId,classId,LocalDateTime.now()) > 0){
          map.put("state",true);
          map.put("msg","申请成功");
        }else{
          map.put("state",true);
          map.put("msg","申请失败");
        }
      }else if(classesDao.checkIsJoinClass(userId,classId) == -1){
        if(classesDao.updateIsPass(userId,classId,LocalDateTime.now()) > 0){
          map.put("state",true);
          map.put("msg","申请成功");
        }else{
          map.put("state",true);
          map.put("msg","申请失败");
        }
      }else{
        map.put("state",true);
        map.put("msg","重复申请");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("state",false);
      map.put("msg","申请发生异常");
    }
    return map;
  }
}
