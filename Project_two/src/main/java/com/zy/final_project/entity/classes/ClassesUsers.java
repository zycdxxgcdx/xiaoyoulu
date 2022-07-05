package com.zy.final_project.entity.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zy.final_project.entity.Users;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ClassesUsers {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long classId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private int role;
//  1 管理员 0 普通成员
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date inClassTime;
  private Users users;
}
