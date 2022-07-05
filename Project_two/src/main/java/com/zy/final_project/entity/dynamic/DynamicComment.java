package com.zy.final_project.entity.dynamic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DynamicComment {
  private int commentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long dynamicId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private String content;
  private LocalDateTime commentTime;
  private String username;
  private String headPosition;
  //计算相差的分钟数  前台显示几分钟前
  private Long timeDifference;
  //相差时间的类型 分钟,小时,天
  private String timeDifferenceType;
  private int nums;
}
