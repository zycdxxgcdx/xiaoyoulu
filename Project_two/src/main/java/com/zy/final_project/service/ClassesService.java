package com.zy.final_project.service;

import com.zy.final_project.entity.classes.Classes;

import java.time.LocalDateTime;
import java.util.Map;

public interface ClassesService {
  Map<String,Object> findAllSchool(String province, String city);
  Map<String,Object> createClass(Classes classes);
  Map<String,Object> findAllClasses(Integer page,Integer size);
  Map<String,Object> findInClasses(Long userId, Integer page, Integer size);
  Map<String,Object> searchClass(String name, Integer page, Integer size);
  Map<String,Object> searchInClass(Long userId, String name, Integer page, Integer size);
  Map<String,Object> findClassInfo(Long classId, Integer page, Integer size);
  Map<String,Object> checkMemberIdentity(Long classId,Long userId);
  Map<String,Object> delClassMember(Long userId, Long classId);
  Map<String,Object> exitClass(Long userId, Long classId);
  Map<String,Object> findClassNameByClassId(Long classId);
  Map<String,Object> joinClass(Long userId, Long classId);
}
