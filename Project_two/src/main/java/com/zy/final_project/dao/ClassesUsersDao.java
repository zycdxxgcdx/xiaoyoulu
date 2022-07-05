package com.zy.final_project.dao;

import com.zy.final_project.entity.classes.ClassesUsers;
import com.zy.final_project.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ClassesUsersDao {
  int createInfo(ClassesUsers classesUsers);
  List<ClassesUsers> findClassInfo(Long classId, Integer page, Integer size);
  ClassesUsers checkMemberIdentity(Long classId,Long userId);
  Integer countMemberClass(Long classId);
  Integer delClassMember(Long userId, Long classId);
  Integer exitClass(Long userId, Long classId);
  List<Users> findUserInfoByClassId(Long classId);
  List<Long> findUserIdByClassId(Long classId);
}
