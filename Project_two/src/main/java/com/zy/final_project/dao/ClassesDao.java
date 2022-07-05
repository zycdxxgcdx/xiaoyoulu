package com.zy.final_project.dao;

import com.zy.final_project.entity.classes.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface ClassesDao {
  List<String> findAllSchool(String province, String city);
  Long selectSchoolId(String province,String city,String schoolName);
  int createClass(Classes classes);
  Long checkClassIsCreated(Classes classes);
  List<Classes> findAllClasses(Integer page, Integer size);
  Integer countAllClass();
  List<Classes> findInClasses(Long userId, Integer page, Integer size);
  Integer countInClass(Long userId);
  List<Classes> searchClass(String name, Integer page, Integer size);
  Integer countsearchClass(String name);
  List<Classes> searchInClass(Long userId, String name, Integer page, Integer size);
  Integer countsearchInClass(Long userId,String name);
  Classes findClassNameByClassId(Long classId);
  List<Classes> findClassNameBySchoolId(Long schoolId,Integer page,Integer size);
  Integer joinClass(Long userId, Long classId, LocalDateTime passTime);
  Integer checkIsJoinClass(Long userId, Long classId);
  Integer updateIsPass(Long userId, Long classId, LocalDateTime passTime);
  Integer getAllClassNumber();
}
