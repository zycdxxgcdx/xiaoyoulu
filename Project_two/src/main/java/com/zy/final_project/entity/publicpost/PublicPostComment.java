package com.zy.final_project.entity.publicpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicPostComment {
  private int commentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long publicpostid;
  private String commentContent;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  private LocalDateTime commentTime;
}
