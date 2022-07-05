package com.zy.final_project.entity.publicpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Emoji {
  private Integer commentId;
  private String commentContent;
  private LocalDateTime commentTime;
  private String username;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private int replyNums;
  private String headPosition;
}
