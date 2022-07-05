package com.zy.final_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Users {
  //解决long型数据前端丢失精度问题
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private String username;
  private String gender;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birth;
  private String headPosition;
}
