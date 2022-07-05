package com.zy.final_project.entity.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Classes {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long classId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long schoolId;
  private String province;
  private String city;
  private String schoolName;
  private String subject;
  private String types;
  private String major;
  private String grade;
  private String className;
  private String headPosition;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
}
