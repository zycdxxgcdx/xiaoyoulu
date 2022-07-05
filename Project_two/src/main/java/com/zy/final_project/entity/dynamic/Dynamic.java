package com.zy.final_project.entity.dynamic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dynamic {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long dynamicId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private String content;
  private LocalDateTime dynamic_time;
  private String submit_images;
  private String username;
  private String headPosition;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long forwardDynamicId;
  //每一条动态评论数
  private int replyNums;
  //每一条动态的转发数
  private int forwardNums;
  //每一条动态的点赞数
  private int likesNums;
  //是否已点赞
  private int islike;
  private Dynamic forwardDynamic;
  //计算相差的分钟数  前台显示几分钟前
  private Long timeDifference;
  //相差时间的类型 分钟,小时,天
  private String timeDifferenceType;
}
