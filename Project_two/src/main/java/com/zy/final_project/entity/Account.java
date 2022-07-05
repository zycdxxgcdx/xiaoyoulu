package com.zy.final_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Account {
  //解决long型数据前端丢失精度问题
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private String accountName;
  private String password;
  private String salt;
  private String role;
}
