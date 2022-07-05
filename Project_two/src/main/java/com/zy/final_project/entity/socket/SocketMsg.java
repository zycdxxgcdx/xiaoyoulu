package com.zy.final_project.entity.socket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocketMsg {
  private int type; //聊天类型0：群聊，1：单聊.
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long fromUser;//发送者
  private String username;
  private String headPosition;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long toUser;//接受者.
  private String content;//消息
  private LocalDateTime time;
  private Integer islast;
}
