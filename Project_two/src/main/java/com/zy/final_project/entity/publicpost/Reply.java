package com.zy.final_project.entity.publicpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reply {
  private int commentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long fromuserId;
  private String fromusername;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long touserId;
  private String touusername;
  private String replycontent;
  private int isat;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long publicpostid;
  private LocalDateTime replyTime;
  private String headPosition;
}
