package com.zy.final_project.dao;

import com.zy.final_project.entity.message.MessageExitClass;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ClassExitDao {
  Integer delByClass(MessageExitClass messageExitClass);
  Integer exitClass(MessageExitClass messageExitClass);
}
