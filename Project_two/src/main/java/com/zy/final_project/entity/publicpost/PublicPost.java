package com.zy.final_project.entity.publicpost;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicPost {
  //解决long型数据前端丢失精度问题
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long publicpostid;
  private String title;   //帖子标题
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;  //发帖用户id
  private String username; //用户名
  private String school; //用户所在学校
  private String content;//帖子内容
//  @JsonFormat(pattern="yyyy-MM-dd hh:mm",timezone="GMT+8")
  private LocalDateTime postdate;  //发帖时间

  private int views;  //浏览量
  private String submit_images;  //图片位置
  private String headPosition; //头像位置
}
