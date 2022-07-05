package com.zy.final_project.dao;

import com.zy.final_project.entity.School;
import com.zy.final_project.entity.publicpost.PublicPost;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostDao {
  //发帖
  void post(PublicPost publicPost);
  List<School> getMySchool(Long userId);
}
