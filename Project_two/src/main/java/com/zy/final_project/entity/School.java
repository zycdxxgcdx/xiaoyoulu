package com.zy.final_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class School {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long schoolId;
  private String province;
  private String city;
  private String schoolName;
  private String headPosition;
}
