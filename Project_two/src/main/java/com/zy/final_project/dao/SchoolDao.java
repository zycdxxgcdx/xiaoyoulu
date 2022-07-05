package com.zy.final_project.dao;

import com.zy.final_project.entity.School;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//扫入service容器里
@Repository
//添加到ioc容器中
public interface SchoolDao {
  int createSchool(School school);
  Long checkSchoolIsCreated(School school);
  List<School> findAllSchool(Integer page,Integer size);
  List<School> findAllInSchool(Long userId, Integer page, Integer size);
  Integer countAllInSchool(Long userId);
  Integer countAllSchool();
  List<String> searchSchoolForName(String name);
  List<String> searchInSchoolForName(Long userId, String name);
  List<School> searchSchool(String name,Integer page,Integer size);
  List<School> searchInSchool(Long userId, String name, Integer page, Integer size);
  Integer countAllSchoolForFuzzySearch(String name);
  Integer countInSchoolForFuzzySearch(Long userId,String name);
  Integer getAllSchoolNumber();
}
