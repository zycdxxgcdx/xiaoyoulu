package com.zy.final_project.entity.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class MessageLike {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long dynamicId;
  private String username;
  private String headPosition;
  private String content;
}
