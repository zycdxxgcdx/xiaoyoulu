package com.zy.final_project.entity.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageExitClass {
  private Integer Id;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private String headPosition;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long classId;
  private String schoolName;
  private String major;
  private String grade;
  private String className;
  private Integer isPass;
  private Integer exitType;
  private LocalDateTime time;
}
