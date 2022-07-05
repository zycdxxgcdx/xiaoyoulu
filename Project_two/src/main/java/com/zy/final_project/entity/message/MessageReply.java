package com.zy.final_project.entity.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageReply {
  //评论或者回复的用户ID
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  //用户名
  private String username;
  //头像
  private String headPosition;
  //评论或者回复的内容
  private String content;
  //评论或者回复的时间
  private LocalDateTime time;
  //评论的那条帖子或者动态或者回复的ID
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long publicpostid;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long dynamicId;
  //评论的那条帖子或者动态或者回复的内容
  private String toContent;
}
