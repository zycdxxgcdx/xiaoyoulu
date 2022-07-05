package com.zy.final_project.entity.socket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocketList {
  private Integer Id;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long fromuserId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long touserId;
  private String username;
  private String schoolName;
  private String major;
  private String grade;
  private String className;
  private String headPosition;
//  0单聊 1群聊 -1用户在聊天列表中删除了此列表
  private Integer type;
  private LocalDateTime time;
}
