package com.zy.final_project.service;

import com.zy.final_project.entity.School;

import java.util.Map;

public interface SchoolService {
  //创建学校
  Map<String,Object> createSchool(School school);
  //查找所有的学校
  Map<String,Object> findAllSchool(Integer page,Integer size);
  Map<String,Object> findAllInSchool(Long userId, Integer page, Integer size);
  Map<String,Object> searchSchoolForName(String name);
  Map<String,Object> searchInSchoolForName(Long userId, String name);
  Map<String,Object> searchSchool(String name,Integer page,Integer size);
  Map<String,Object> searchInSchool(Long userId, String name, Integer page, Integer size);
  Map<String,Object> findSchoolInfo(Long schoolId, Integer page, Integer size);
}
