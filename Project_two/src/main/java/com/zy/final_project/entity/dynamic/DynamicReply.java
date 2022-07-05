package com.zy.final_project.entity.dynamic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DynamicReply {
  private int dynamicReplyId;
  private int commentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long fromuserId;
  private String fromusername;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long touserId;
  private String touusername;
  private String content;
  private LocalDateTime reply_time;
  private String username;
  private String headPosition;
  private Integer isat;
  //计算相差的分钟数  前台显示几分钟前
  private Long timeDifference;
  //相差时间的类型 分钟,小时,天
  private String timeDifferenceType;
  //回复总数
  private int nums;
}
